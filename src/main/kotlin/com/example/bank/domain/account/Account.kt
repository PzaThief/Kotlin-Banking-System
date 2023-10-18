package com.example.bank.domain.account

import java.math.BigDecimal
import java.math.BigInteger

class Account(
    val id: AccountId,
    val ownerName: String,
    val accountProduct: AccountProduct,
    val initialDeposit: BigDecimal,
) {
    var balance = initialDeposit
        private set(value) {
            if (value < BigDecimal.ZERO) {
                throw error("balance can't be negative value")
            }
            field = value
        }
    val displayId = AccountDisplayId("${this.accountProduct.code}-${this.id}")
}

data class AccountId(val value: String)
data class AccountDisplayId(val value: String)