package com.example.bank.account.application

import com.example.bank.account.domain.Account
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountCreateRequest(
    val ownerId: Long,
    val accountProduct: String,
    val initialDeposit: BigDecimal
)

data class AccountResponse(
    val id: Long,
    val displayId: String,
    val ownerId: Long,
    val accountProduct: String,
    val initialDeposit: BigDecimal,
    val balance: BigDecimal,
    val updatedAt: LocalDateTime?,
    val createdAt: LocalDateTime?
) {
    constructor(account: Account) : this(
        account.id.value,
        account.displayId.value,
        account.ownerId,
        account.accountProduct.code,
        account.initialDeposit,
        account.balance,
        account.updatedAt,
        account.createdAt,
    )
}

data class AccountTransferRequest(
    val fromAccountId: Long,
    val toAccountId: Long,
    val amount: BigDecimal
)