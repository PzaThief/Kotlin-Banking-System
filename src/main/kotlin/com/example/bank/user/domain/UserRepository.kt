package com.example.bank.user.domain

interface UserRepository {
    fun findById(userId: User.Id): User

    fun findByLoginId(loginId: String): User

    fun saveAndFlush(user: User): User

}