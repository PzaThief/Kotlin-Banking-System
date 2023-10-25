package com.example.bank.user.application

import com.example.bank.user.domain.User
import com.example.bank.user.domain.UserFactory
import com.example.bank.user.infrastructure.UserJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


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
        return userRepository.findById(User.Id(userId))
            .let { UserResponse(it) }
    }

}