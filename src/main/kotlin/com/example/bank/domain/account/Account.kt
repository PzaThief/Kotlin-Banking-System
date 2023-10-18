package com.example.bank.domain.account

import java.math.BigDecimal

class Account(
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

    fun displayId(): String = "${this.accountProduct.code}-0000001"
}