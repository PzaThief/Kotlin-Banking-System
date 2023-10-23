package com.example.bank.account.domain

import java.math.BigDecimal

interface NextAccountIdGenerator {
    fun nextAccountId(): Account.Id
}
class AccountFactory(
    private val nextAccountIdGenerator: NextAccountIdGenerator
) {
    suspend fun createAccount(
        ownerName: String,
        accountProduct: AccountProduct,
        initialDeposit: BigDecimal
    ): Account {
        val accountId = suspend { nextAccountIdGenerator.nextAccountId() }()
        val displayId = Account.DisplayId("${accountProduct.code}-${accountId.value.toString().padStart(7, '0')}")

        return Account(
            id = accountId,
            displayId = displayId,
            ownerName = ownerName,
            accountProduct = accountProduct,
            initialDeposit = initialDeposit,
        )
    }
}