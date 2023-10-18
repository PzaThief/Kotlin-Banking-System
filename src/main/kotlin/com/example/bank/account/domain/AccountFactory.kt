package com.example.bank.account.domain

import java.math.BigDecimal

class AccountFactory(
    private val accountRepository: AccountRepository
) {
    fun createAccount(
        ownerName: String,
        accountProduct: AccountProduct,
        initialDeposit: BigDecimal
    ): Account {
        return Account(
            id = accountRepository.nextAccountId(),
            ownerName = ownerName,
            accountProduct = accountProduct,
            initialDeposit = initialDeposit
        )
    }
}