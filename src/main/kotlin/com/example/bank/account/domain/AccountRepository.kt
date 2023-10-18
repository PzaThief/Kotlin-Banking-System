package com.example.bank.account.domain

interface AccountRepository {
    fun find(accountId: AccountId): Account?

    fun find(accountDisplayId: AccountDisplayId): Account?

    fun save(account: Account)

    fun nextAccountId(): AccountId
}