package com.example.bank.account.domain

interface AccountRepository {
    fun findById(accountId: Account.Id): Account?

    fun findByDisplayId(accountDisplayId: Account.DisplayId): Account?

    fun saveAndFlush(account: Account): Account
}