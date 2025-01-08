package kr.co.pincoin.api.global.security.password

import org.springframework.security.crypto.password.PasswordEncoder
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class DjangoPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence): String = try {
        encode(rawPassword.toString(), getSalt())
    } catch (e: Exception) {
        throw IllegalStateException("Password encoding failed", e)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        val parts = encodedPassword.split(HASH_SECTIONS_DELIMITER)

        if (parts.size != EXPECTED_SECTIONS_LENGTH) return false

        return try {
            encode(
                plain = rawPassword.toString(),
                salt = parts[2],
                iterations = parts[1].toInt()
            ) == encodedPassword
        } catch (e: Exception) {
            false
        }
    }

    private fun encode(
        plain: String,
        salt: String,
        iterations: Int = DEFAULT_ITERATIONS
    ): String {
        val hash = encrypt(plain, salt, iterations)
        return PASSWORD_HASH_FORMAT.format(ALGORITHM, iterations, salt, hash)
    }

    private fun encrypt(plain: String, salt: String, iterations: Int): String {
        val keySpec = PBEKeySpec(
            plain.toCharArray(),
            salt.toByteArray(StandardCharsets.UTF_8),
            iterations,
            HASH_LENGTH * BITS_PER_BYTE
        )

        return SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
            .generateSecret(keySpec)
            .encoded
            .let { Base64.getEncoder().encodeToString(it) }
    }

    companion object {
        private const val ALGORITHM = "pbkdf2_sha256"
        private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
        private const val DEFAULT_ITERATIONS = 870_000
        private const val SALT_LENGTH = 16
        private const val HASH_LENGTH = 32
        private const val BITS_PER_BYTE = 8
        private const val PASSWORD_HASH_FORMAT = "%s$%d$%s$%s"
        private const val HASH_SECTIONS_DELIMITER = "$"
        private const val EXPECTED_SECTIONS_LENGTH = 4

        private val secureRandom = SecureRandom()

        private fun getSalt(length: Int = SALT_LENGTH): String {
            val saltBytes = ByteArray(length).apply {
                secureRandom.nextBytes(this)
            }
            return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(saltBytes)
        }
    }
}