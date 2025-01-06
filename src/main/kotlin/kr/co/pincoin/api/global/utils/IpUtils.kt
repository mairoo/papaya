package kr.co.pincoin.api.global.utils

import jakarta.servlet.http.HttpServletRequest

object IpUtils {
    private val IP_HEADERS = listOf(
        "CF-Connecting-IP",
        "X-Real-IP",
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_CLIENT_IP"
    )

    fun getClientIp(request: HttpServletRequest): String =
        IP_HEADERS
            .firstNotNullOfOrNull { header ->
                request.getHeader(header)?.takeUnless { it.equals("unknown", ignoreCase = true) }
                    ?.let { ip ->
                        when (header) {
                            "X-Forwarded-For" -> ip.split(",").first()
                            else -> ip
                        }
                    }
            } ?: request.remoteAddr
}
