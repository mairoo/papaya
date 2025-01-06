package kr.co.pincoin.api.domain.auth.repository.user

import kr.co.pincoin.api.domain.auth.model.user.User

interface UserRepository {
    fun save(user: User): User

    fun delete(user: User)

    fun findById(id: Int): User?

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean
}