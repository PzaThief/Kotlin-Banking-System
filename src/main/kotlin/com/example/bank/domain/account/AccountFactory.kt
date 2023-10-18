package com.example.bank.domain.account

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