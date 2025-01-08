package kr.co.pincoin.api.app.auth.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccessTokenResponse(
    @JsonProperty("accessToken")
    val accessToken: String,

    // OAuth 2.0에서 가장 일반적인 타입
    @JsonProperty("tokenType")
    val tokenType: String = "Bearer",

    // 초 단위의 만료 시간
    @JsonProperty("expiresIn")
    val expiresIn: Int
) {
    companion object {
        fun of(accessToken: String, expiresIn: Int) = AccessTokenResponse(
            accessToken = accessToken,
            expiresIn = expiresIn
        )
    }
}