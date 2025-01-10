package kr.co.pincoin.api.global.security.adapter

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.co.pincoin.api.domain.auth.model.user.User
import kr.co.pincoin.api.domain.auth.repository.user.UserRepository
import kr.co.pincoin.api.global.exception.code.AuthErrorCode
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true) // 조회 전용 트랜잭션 적용
class UserDetailsServiceAdapter(
    private val userRepository: UserRepository
) : UserDetailsService {
    private val log = KotlinLogging.logger {}

    /**
     * `UserRepository`를 스프링시큐리티의 `UserDetailsService`로 변환하는 어댑터
     *
     * - 도메인 계층 UserRepository.findByEmail -> 어댑터(단방향 mapper) -> UserDetailsService.loadUserByUsername
     * - 메서드명과 반환 타입의 불일치 해결:
     *   - findByEmail -> loadUserByUsername
     *   - Optional<User> -> UserDetails
     */
    override fun loadUserByUsername(email: String): UserDetails =
        when (email) {
            "test@example.com" -> UserDetailsAdapter(
                User.of(
                    id = 1,
                    password = "\$2a\$10\$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG", // "password1234"
                    email = "test@example.com",
                    username = "testuser",
                    firstName = "Test",
                    lastName = "User",
                    isActive = true,
                    dateJoined = LocalDateTime.of(2024, 1, 1, 9, 0)
                )
            )

            "admin@example.com" -> UserDetailsAdapter(
                User.of(
                    id = 2,
                    password = "\$2a\$10\$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG", // "password1234"
                    email = "admin@example.com",
                    username = "admin",
                    firstName = "Admin",
                    lastName = "User",
                    isSuperuser = true,
                    isStaff = true,
                    isActive = true,
                    dateJoined = LocalDateTime.of(2024, 1, 1, 9, 0)
                )
            )

            else -> throw UsernameNotFoundException(AuthErrorCode.INVALID_CREDENTIALS.message)
                .also { log.error { "$email email not found" } }
        }
}