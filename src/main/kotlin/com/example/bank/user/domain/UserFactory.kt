package com.example.bank.user.domain

import org.mindrot.jbcrypt.BCrypt

interface NextUserIdGenerator {
    fun nextUserId(): User.Id
}

class UserFactory(
    private val nextUserIdGenerator: NextUserIdGenerator
) {
    suspend fun createUser(
        loginId: String,
        loginPassword: String,
        name: String,
        personCategory: String,
        registrationNumber: String,
    ): User {
        val userId = suspend { nextUserIdGenerator.nextUserId() }()
        val hashedPassword = BCrypt.hashpw(loginPassword, BCrypt.gensalt())
        val encryptedRegistrationNumber = Aes256Encryptor.encrypt(registrationNumber)

        return User(
            id = userId,
            loginId = loginId,
            loginPassword = hashedPassword,
            name = name,
            personCategory = UserPersonCategory.valueOfIgnoreCase(personCategory),
            registrationNumber = encryptedRegistrationNumber,
        )
    }
}