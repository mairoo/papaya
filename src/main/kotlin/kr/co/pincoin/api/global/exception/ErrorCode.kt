package kr.co.pincoin.api.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 리소스에 대한 권한이 없습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다"),
}