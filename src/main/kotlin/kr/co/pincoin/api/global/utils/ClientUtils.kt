package kr.co.pincoin.api.global.utils

import jakarta.servlet.http.HttpServletRequest

// 싱글톤 구현으로 단 하나의 인스턴스만 존재
// 자바에서 모든 메소드가 public static 메소드로 모은 유틸리티 함수, 상수애 대응
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