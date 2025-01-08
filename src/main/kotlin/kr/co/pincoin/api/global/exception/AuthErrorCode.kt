package kr.co.pincoin.api.global.exception

import org.springframework.http.HttpStatus

// 인증/인가 관련 에러
enum class AuthErrorCode(
    override val status: HttpStatus,
    override val message: String,

    ) : ErrorCode {
    FORBIDDEN(
        HttpStatus.FORBIDDEN,
        "해당 리소스에 대한 권한이 없습니다",
    ),
    UNAUTHORIZED(
        HttpStatus.UNAUTHORIZED,
        "로그인이 필요한 서비스입니다",
    ),
}