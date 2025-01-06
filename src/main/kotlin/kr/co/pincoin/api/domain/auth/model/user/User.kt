package kr.co.pincoin.api.domain.auth.model.user

import kr.co.pincoin.api.app.member.user.request.UserCreateRequest
import java.time.LocalDateTime

class User private constructor(
    // 식별자 및 생성일 (불변)
    val id: Int?,
    val dateJoined: LocalDateTime,

    // 인증 관련 (가변)
    var password: String,
    val email: String,
    var lastLogin: LocalDateTime?,

    // 개인정보 (가변)
    val username: String,
    var firstName: String,
    var lastName: String,

    // 권한 관련 (가변)
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

        fun createUser(
            email: String,
            password: String,
            username: String,
            firstName: String = "",
            lastName: String = "",
        ): User = of(
            email = email,
            password = password,
            username = username,
            firstName = firstName,
            lastName = lastName
        )

        fun createAdmin(
            email: String,
            password: String,
            username: String,
            firstName: String = "",
            lastName: String = "",
        ): User = of(
            email = email,
            password = password,
            username = username,
            firstName = firstName,
            lastName = lastName,
            isSuperuser = true,
            isStaff = true
        )

        fun from(request: UserCreateRequest): User {
            return of(
                email = request.email,
                password = request.password,
                username = request.username,
                firstName = request.firstName,
                lastName = request.lastName
            )
        }
    }
}