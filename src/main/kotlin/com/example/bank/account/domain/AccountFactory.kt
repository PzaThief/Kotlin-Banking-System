package com.example.bank.account.domain

import java.math.BigDecimal

interface NextAccountIdGenerator {
    fun nextAccountId(): Account.Id
}

class AccountFactory(
    private val nextAccountIdGenerator: NextAccountIdGenerator
) {
    suspend fun createAccount(
        ownerId: Long,
        accountProduct: AccountProduct,
        initialDeposit: BigDecimal
    ): Account {
        val accountId = suspend { nextAccountIdGenerator.nextAccountId() }()

        return Account(
            id = accountId,
            displayId = newDisplayId(accountProduct, accountId),
            ownerId = ownerId,
            accountProduct = accountProduct,
            initialDeposit = initialDeposit,
        )
    }

    private fun newDisplayId(accountProduct: AccountProduct, accountId: Account.Id): Account.DisplayId {
        return Account.DisplayId("${accountProduct.code}-${accountId.value.toString().padStart(7, '0')}")
    }
}