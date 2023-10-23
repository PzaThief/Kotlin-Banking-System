package com.example.bank.account.infrastructure.jpa

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountRepository
import com.example.bank.account.domain.NextAccountIdGenerator
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.math.BigInteger

interface AccountJpaRepository: CrudRepository<Account, BigInteger>, AccountRepository, NextAccountIdGenerator {
    override fun findByDisplayId(accountDisplayId: Account.DisplayId): Account

    @Query(value = "SELECT nextval('seq_account')", nativeQuery = true)
    fun nextAccountIdString(): String

    override fun nextAccountId() = Account.Id(this.nextAccountIdString().toLong())
}