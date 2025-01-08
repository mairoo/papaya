package kr.co.pincoin.api.global.exception

import kr.co.pincoin.api.global.exception.code.ErrorCode

data class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)