package kr.co.pincoin.api.domain.auth.repository.user

import kr.co.pincoin.api.domain.auth.model.user.User
import kr.co.pincoin.api.infra.auth.repository.user.UserRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime

@DataJpaTest
@Import(UserRepositoryImpl::class)
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepositoryImpl

    @Test
    fun `findById should return user when user exists`() {
        // Given
        val user = User.of(
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            isStaff = false,
            isActive = true,
            dateJoined = LocalDateTime.now(),
            lastLogin = LocalDateTime.now()
        )
        val savedUser = userRepository.save(user)

        // When
        val foundUser = userRepository.findById(savedUser.id!!)

        // Then
        assertThat(foundUser).isNotNull
        assertThat(foundUser?.id).isEqualTo(savedUser.id)
        assertThat(foundUser?.email).isEqualTo("test@example.com")
        assertThat(foundUser?.username).isEqualTo("testuser")
    }

    @Test
    fun `findById should return null when user does not exist`() {
        // When
        val foundUser = userRepository.findById(999)

        // Then
        assertThat(foundUser).isNull()
    }
}