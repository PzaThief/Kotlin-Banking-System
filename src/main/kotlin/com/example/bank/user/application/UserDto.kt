package com.example.bank.user.application

import com.example.bank.user.domain.User
import java.time.LocalDateTime


data class UserCreateRequest(
    val loginId: String,
    val loginPassword: String,
    val name: String,
    val personCategory: String,
    val registrationNumber: String,
)

data class UserResponse(
    val id: Long,
    val loginId: String,
    val name: String,
    val personCategory: String,
    val updatedAt: LocalDateTime?,
    val createdAt: LocalDateTime?,
) {
    constructor(user: User) : this(
        user.id.value,
        user.loginId,
        user.name,
        user.personCategory.code,
        user.updatedAt,
        user.createdAt,
    )
}