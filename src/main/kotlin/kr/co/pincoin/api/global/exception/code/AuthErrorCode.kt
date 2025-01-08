package kr.co.pincoin.api.global.exception.code

import org.springframework.http.HttpStatus

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
    EXPIRED_TOKEN(
        HttpStatus.UNAUTHORIZED,
        "만료된 토큰입니다",
    ),
    INVALID_TOKEN(
        HttpStatus.UNAUTHORIZED,
        "유효하지 않은 토큰입니다",
    ),
    UNEXPECTED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "알 수 없는 오류",
    ),
}