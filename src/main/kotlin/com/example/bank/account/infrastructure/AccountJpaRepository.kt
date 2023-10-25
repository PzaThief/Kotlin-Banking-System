package com.example.bank.account.infrastructure

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountRepository
import com.example.bank.account.domain.NextAccountIdGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigInteger

interface AccountJpaRepository : JpaRepository<Account, BigInteger>, AccountRepository, NextAccountIdGenerator {
    override fun findByDisplayId(accountDisplayId: Account.DisplayId): Account

    @Query(value = "SELECT nextval('seq_account')", nativeQuery = true)
    fun nextAccountIdString(): String
    override fun nextAccountId() = Account.Id(this.nextAccountIdString().toLong())
}