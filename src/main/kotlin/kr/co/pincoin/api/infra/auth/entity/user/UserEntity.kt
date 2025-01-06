package kr.co.pincoin.api.infra.auth.entity.user

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "auth_user")
class UserEntity private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int? = null,

    @Column(name = "date_joined")
    val dateJoined: LocalDateTime = LocalDateTime.now(),

    @Column(name = "password")
    val password: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "last_login")
    val lastLogin: LocalDateTime? = null,

    @Column(name = "username")
    val username: String,

    @Column(name = "first_name")
    val firstName: String = "",

    @Column(name = "last_name")
    val lastName: String = "",

    @Column(name = "is_superuser")
    val isSuperuser: Boolean = false,

    @Column(name = "is_staff")
    val isStaff: Boolean = false,

    @Column(name = "is_active")
    val isActive: Boolean = true,
) {
    companion object {
        fun of(
            id: Int? = null,
            password: String,
            username: String,
            email: String,
            lastLogin: LocalDateTime? = null,
            isSuperuser: Boolean = false,
            firstName: String = "",
            lastName: String = "",
            isStaff: Boolean = false,
            isActive: Boolean = true,
            dateJoined: LocalDateTime = LocalDateTime.now(),
        ) = UserEntity(
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
            isActive = isActive,
        )
    }
}