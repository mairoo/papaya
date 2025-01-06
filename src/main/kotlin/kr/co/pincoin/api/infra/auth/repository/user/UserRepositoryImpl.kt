package kr.co.pincoin.api.infra.auth.repository.user

import kr.co.pincoin.api.domain.auth.model.user.User
import kr.co.pincoin.api.domain.auth.repository.user.UserRepository
import kr.co.pincoin.api.infra.auth.mapper.user.toEntity
import kr.co.pincoin.api.infra.auth.mapper.user.toModel
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val jpaRepository: UserJpaRepository) : UserRepository {
    override fun save(user: User): User = jpaRepository.save(user.toEntity()!!).toModel()!!

    override fun delete(user: User) = jpaRepository.delete(user.toEntity()!!)

    override fun findById(id: Int): User? = jpaRepository.findById(id).orElse(null)?.toModel()

    override fun findByEmail(email: String): User? = jpaRepository.findByEmail(email)?.toModel()

    override fun existsByEmail(email: String): Boolean = jpaRepository.existsByEmail(email)

    override fun existsByUsername(username: String): Boolean =
        jpaRepository.existsByUsername(username)
}