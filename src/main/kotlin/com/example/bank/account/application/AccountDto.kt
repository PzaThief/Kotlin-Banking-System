package com.example.bank.account.application

import com.example.bank.account.domain.Account
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountCreateRequest(
    val ownerName: String,
    val accountProduct: String,
    val initialDeposit: BigDecimal
)

data class AccountResponse(
    val id: Long,
    val displayId: String,
    val ownerName: String,
    val accountProduct: String,
    val initialDeposit: BigDecimal,
    val balance: BigDecimal,
    val updatedAt: LocalDateTime?,
    val createdAt: LocalDateTime?
) {
    constructor(account: Account) : this(
        account.id.value,
        account.displayId.value,
        account.ownerName,
        account.accountProduct.code,
        account.initialDeposit,
        account.balance,
        account.updatedAt,
        account.createdAt,
    )
}
