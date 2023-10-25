package com.example.bank.user

import com.example.bank.user.domain.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class UserDomainTests {

    @Nested
    inner class CreateUser {
        @Test
        fun createUserShouldSameWithGivenInfo() {
            class NextUserIdGeneratorImpl : NextUserIdGenerator {
                var id = 1L
                override fun nextUserId() = User.Id(id++)
            }
            runBlocking {
                val loginId = "kotlin"
                val loginPassword = "bank"
                val name = "홍길동"
                val personCategory = "1"
                val registrationNumber = "990123-1012345"

                val userFactory = UserFactory(NextUserIdGeneratorImpl())
                val user = userFactory.createUser(loginId, loginPassword, name, personCategory, registrationNumber)

                assert(loginId == user.loginId)
                assert(BCrypt.checkpw(loginPassword,user.loginPassword))
                assert(name == user.name)
                assert(user.personCategory == UserPersonCategory.NATURAL)
                assert(registrationNumber == Aes256Encryptor.decrypt(user.registrationNumber))
            }
        }
    }
}
