package kr.co.pincoin.api.domain.auth.model.user

import kr.co.pincoin.api.app.member.user.request.UserCreateRequest
import java.time.LocalDateTime

class User private constructor(
    // 불변
    // 식별자 및 가입일시
    val id: Int?,
    val dateJoined: LocalDateTime,

    // 가변
    // 인증정보
    var password: String,
    val email: String,
    var lastLogin: LocalDateTime?,

    // 개인정보
    val username: String,
    var firstName: String,
    var lastName: String,

    // 권한 관련
    var isSuperuser: Boolean,
    var isStaff: Boolean,
    var isActive: Boolean,
) {
    companion object {
        fun of(
            id: Int? = null,
            dateJoined: LocalDateTime = LocalDateTime.now(),
            password: String,
            email: String,
            lastLogin: LocalDateTime? = null,
            username: String,
            firstName: String = "",
            lastName: String = "",
            isSuperuser: Boolean = false,
            isStaff: Boolean = false,
            isActive: Boolean = true,
        ): User = User(
            id = id,
            dateJoined = dateJoined,
            password = password,
            email = email,
            lastLogin = lastLogin,
            username = username,
            firstName = firstName,
            lastName = lastName,
            isSuperuser = isSuperuser,
            isStaff = isStaff,
            isActive = isActive
        )

        fun from(request: UserCreateRequest, isAdmin: Boolean = false): User = of(
            email = request.email,
            password = request.password,
            username = request.username,
            firstName = request.firstName,
            lastName = request.lastName,
            isSuperuser = isAdmin,
            isStaff = isAdmin
        )
    }
}