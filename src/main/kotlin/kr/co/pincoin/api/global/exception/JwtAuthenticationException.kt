package kr.co.pincoin.api.global.exception

import kr.co.pincoin.api.global.exception.code.ErrorCode

data class JwtAuthenticationException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message)