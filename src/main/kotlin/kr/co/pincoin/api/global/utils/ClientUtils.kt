package kr.co.pincoin.api.global.utils

import jakarta.servlet.http.HttpServletRequest

object ClientUtils {
    private const val USER_AGENT_HEADER = "User-Agent"
    private const val ACCEPT_LANGUAGE_HEADER = "Accept-Language"

    data class ClientInfo(
        val userAgent: String = "",
        val acceptLanguage: String = "",
        val ipAddress: String
    )

    fun getClientInfo(request: HttpServletRequest) = ClientInfo(
        userAgent = request.getHeader(USER_AGENT_HEADER).orEmpty(),
        acceptLanguage = request.getHeader(ACCEPT_LANGUAGE_HEADER).orEmpty(),
        ipAddress = IpUtils.getClientIp(request)
    )
}