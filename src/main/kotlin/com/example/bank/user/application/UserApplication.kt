package com.example.bank.user.application

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserApplication(
//    @Autowired
//    val userRepository: UserJpaRepository
) {
//    val userFactory = UserFactory(userRepository)
    suspend fun createUser(userCreateRequest: UserCreateRequest): UserResponse {
        return Any() as UserResponse
    }

    suspend fun getUser(userId: Long): UserResponse {
        return Any() as UserResponse
    }

}