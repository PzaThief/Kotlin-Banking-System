package com.example.bank.user.infrastructure

import com.example.bank.user.domain.NextUserIdGenerator
import com.example.bank.user.domain.User
import com.example.bank.user.domain.UserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserJpaRepository : JpaRepository<User, Long>, UserRepository, NextUserIdGenerator {
    override fun findByLoginId(loginId: String): User?

    @Query(value = "SELECT nextval('seq_user')", nativeQuery = true)
    fun nextUserIdString(): String
    override fun nextUserId() = User.Id(this.nextUserIdString().toLong())
}