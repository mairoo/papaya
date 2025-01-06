package kr.co.pincoin.api.app.member.user.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserCreateRequest(
    @field:NotBlank(message = "이메일은 필수 입력값입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    @field:Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다")
    @JsonProperty("email")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력값입니다")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
        message = "비밀번호는 8~30자리이면서 영문, 숫자, 특수문자를 포함해야 합니다"
    )
    @JsonProperty("password")
    val password: String,

    @field:NotBlank(message = "아이디는 필수 입력값입니다")
    @field:Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하여야 합니다")
    @field:Pattern(
        regexp = "^[a-z0-9][a-z0-9-_]{3,19}$",
        message = "아이디는 영문 소문자, 숫자로 시작하고 영문 소문자, 숫자, 하이픈, 언더스코어만 사용하여 4~20자리여야 합니다"
    )
    @JsonProperty("username")
    val username: String,

    @field:NotBlank(message = "이름을 입력해주세요")
    @field:Size(min = 1, max = 50, message = "이름은 1자 이상 50자 이하로 입력해주세요")
    @JsonProperty("firstName")
    val firstName: String,

    @field:NotBlank(message = "성을 입력해주세요")
    @field:Size(min = 1, max = 50, message = "성은 1자 이상 50자 이하로 입력해주세요")
    @JsonProperty("lastName")
    val lastName: String
)