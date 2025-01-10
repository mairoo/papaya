package kr.co.pincoin.api.global.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.pincoin.api.global.exception.JwtAuthenticationException
import kr.co.pincoin.api.global.exception.code.AuthErrorCode
import kr.co.pincoin.api.global.response.error.ErrorResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService,
    private val objectMapper: ObjectMapper,
    @Value("\${bank-transfer.token}") private val bankTransferToken: String,
) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}

    companion object {
        private val BANK_TRANSFER_WEBHOOK_PATH = AntPathRequestMatcher("/payment/bank-transfer/callback")

        private val SWAGGER_PATHS = listOf(
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/v3/api-docs/**")
        )

        const val BEARER_PREFIX = "Bearer "
        const val TOKEN_PREFIX = "Token "

        // 매 요청마다 JWT 검증 오버헤드 발생 - 불필요한 토큰 파싱과 서명 검증
        // 스프링 시큐리티 설정에서 permitAll() 한 경로라고 해서 토큰 검증 필터가 실행이 안 되는 것이 아님
        //
        // 1. JwtAuthenticationFilter (토큰 검증)
        // 2. UsernamePasswordAuthenticationFilter
        // 3. ... 다른 필터들 ...
        // 4. FilterSecurityInterceptor (URL 기반 permitAll() 등의 권한 설정

        // JWT 토큰 검증이 불필요한 공개 엔드포인트 목록
        private val publicMatchers = listOf(
            AntPathRequestMatcher("/auth/**"),
            AntPathRequestMatcher("/oauth2/**"),
            AntPathRequestMatcher("/actuator/**"),
            AntPathRequestMatcher("/products/**"),
            AntPathRequestMatcher("/categories/**"),
            AntPathRequestMatcher("/public/**"),
            AntPathRequestMatcher("/api/**"),
            AntPathRequestMatcher("/payment/billgate/callback"),
            AntPathRequestMatcher("/payment/paypal/callback"),
            AntPathRequestMatcher("/payment/danal/callback")
        )
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val finalMatchers = publicMatchers +
                if (!environment.activeProfiles.contains("prod")) SWAGGER_PATHS
                else emptyList()

        return finalMatchers.any { it.matches(request) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val isWebhookPath = BANK_TRANSFER_WEBHOOK_PATH.matches(request)
            val bearerToken = request.getBearerToken()
            val apiToken = request.getToken()

            when {
                apiToken == null && bearerToken == null ->
                    throw JwtAuthenticationException(AuthErrorCode.UNAUTHORIZED)

                isWebhookPath -> processApiToken(apiToken)
                else -> processBearerToken(bearerToken)
            }

            filterChain.doFilter(request, response)
        } catch (e: JwtAuthenticationException) {
            SecurityContextHolder.clearContext()
            handleAuthenticationException(request, response, e)
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
            handleAuthenticationException(
                request,
                response,
                JwtAuthenticationException(AuthErrorCode.UNAUTHORIZED)
            )
        }
    }

    private fun processBearerToken(token: String?) {
        // JWT 토큰 유효성 확인 및 username 추출 후 인증 처리
        token?.let {
            jwtTokenProvider.validateToken(it)
                ?.let { username -> authenticateUser(username) }
        }
    }

    private fun processApiToken(token: String?) {
        if (token != bankTransferToken) {
            throw JwtAuthenticationException(AuthErrorCode.INVALID_CREDENTIALS)
        }

        // API 토큰이 유효한 경우 아무 작업도 하지 않음
        // - 보안 컨텍스트에 인증 정보를 저장하지 않음
        log.debug { "Valid API token provided" }
    }

    private fun authenticateUser(username: String) {
        try {
            // 1. 데이터베이스에서 username 조회
            val userDetails = userDetailsService.loadUserByUsername(username)

            // 2. 인증 객체 생성
            val auth: Authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            // 3. WebAuthenticationDetails, WebAuthenticationDetailsSource 저장
            // WebAuthenticationDetails 객체는 인증 요청과 관련된 웹 관련 정보
            // - RemoteAddress (클라이언트 IP 주소)
            // - SessionId (현재 세션 ID)
            // setDetails()의 활용 용도
            // - 특정 IP 주소나 지역에서의 접근 제한
            // - 세션 기반의 추가적인 보안 검증
            // - 사용자 행동 분석 및 로깅
            // - 감사(audit) 기록 생성
            // - 다중 요소 인증(MFA) 구현
            // - IP 기반 접근 제한이나 차단
            // authentication.setDetails(new
            // WebAuthenticationDetailsSource().buildDetails(request));

            // 4. 현재 인증된 사용자 정보를 보안 컨텍스트에 저장 = 로그인 처리
            SecurityContextHolder.getContext().authentication = auth
            log.debug { "logged in: ${auth.principal}" }
        } catch (e: UsernameNotFoundException) {
            throw JwtAuthenticationException(AuthErrorCode.INVALID_CREDENTIALS)
        }
    }

    private fun HttpServletRequest.getBearerToken(): String? =
        getHeader(HttpHeaders.AUTHORIZATION)?.let { header ->
            // Header format
            // RFC 7235 standard header
            // Authorization: Bearer JWTString=
            if (header.startsWith(BEARER_PREFIX)) {
                header.substring(BEARER_PREFIX.length).trim()
            } else null
        }

    private fun HttpServletRequest.getToken(): String? =
        getHeader(HttpHeaders.AUTHORIZATION)?.let { header ->
            if (header.startsWith(TOKEN_PREFIX)) {
                header.substring(TOKEN_PREFIX.length).trim()
            } else null
        }

    private fun handleAuthenticationException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: JwtAuthenticationException
    ) {
        response.apply {
            status = HttpStatus.UNAUTHORIZED.value()
            contentType = "${MediaType.APPLICATION_JSON_VALUE};charset=UTF-8"
            characterEncoding = "UTF-8"
        }

        log.warn { "[Authentication] 인증실패: ${e.message}" }

        val errorResponse = ErrorResponse.of(
            request,
            AuthErrorCode.UNAUTHORIZED.status,
            AuthErrorCode.UNAUTHORIZED.message
        )

        objectMapper.writeValue(response.writer, errorResponse)
    }
}