package com.example.bank.user.application

import com.example.bank.user.domain.User
import com.example.bank.user.domain.UserFactory
import com.example.bank.user.infrastructure.UserJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class UserApplication(
    @Autowired
    val userRepository: UserJpaRepository
) {
    val userFactory = UserFactory(userRepository)
    suspend fun createUser(userCreateRequest: UserCreateRequest): UserResponse {
        return userFactory.createUser(
            userCreateRequest.loginId,
            userCreateRequest.loginPassword,
            userCreateRequest.name,
            userCreateRequest.personCategory,
            userCreateRequest.registrationNumber
        )
            .let { userRepository.saveAndFlush(it) }
            .let { UserResponse(it) }
    }

    suspend fun getUser(userId: Long): UserResponse {
        val user = userRepository.findById(User.Id(userId))
            ?: throw Exception("cannot find user by id $userId")

        return UserResponse(user)
    }

    suspend fun updateUserBalance(userId: Long, changeAmount: BigDecimal) {
        val user = userRepository.findById(User.Id(userId))
            ?: throw Exception("cannot find user by id $userId")

        user.changeBalance(changeAmount)
        userRepository.saveAndFlush(user)
    }
}