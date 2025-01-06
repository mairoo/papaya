package kr.co.pincoin.api.infra.auth.repository.user

import kr.co.pincoin.api.infra.auth.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Int> {
    fun findByUsername(username: String): UserEntity?

    fun findByEmail(email: String): UserEntity?

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean
}