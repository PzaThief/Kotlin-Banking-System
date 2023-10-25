package com.example.bank.user

import com.example.bank.user.domain.User
import com.example.bank.user.infrastructure.UserJpaRepository
import com.example.bank.config.JpaAuditingConfig
import com.example.bank.user.domain.Aes256Encryptor
import com.example.bank.user.domain.UserPersonCategory
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@Import(JpaAuditingConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTests(
    @Autowired
    private val userRepository: UserJpaRepository
) {

    @Test
    fun nextUserIdShouldIncrease() {
        val firstUserId = userRepository.nextUserId()
        val secondUserId = userRepository.nextUserId()
        assert(firstUserId.value < secondUserId.value)
    }

    @Test
    fun jpaAuditShouldGenerateCreatedAtAndUpdatedAt() {
        val hashedPassword = BCrypt.hashpw("bank", BCrypt.gensalt())
        val encryptedRegistrationNumber = Aes256Encryptor.encrypt("990123-1012345")

        val user = userRepository.saveAndFlush(
            User(
                id = User.Id(1),
                loginId = "kotlin",
                loginPassword = hashedPassword,
                name = "홍길동",
                personCategory = UserPersonCategory.NATURAL,
                registrationNumber = encryptedRegistrationNumber,
            )
        )
        assertNotNull(user.createdAt)

        val lastUpdatedAt = user.updatedAt
        user.loginPassword = BCrypt.hashpw("bank123", BCrypt.gensalt())
        userRepository.saveAndFlush(user)
        assert(user.updatedAt!! > lastUpdatedAt)
    }
}