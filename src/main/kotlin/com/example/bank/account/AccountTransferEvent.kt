package com.example.bank.account

import org.springframework.modulith.events.Externalized
import java.math.BigDecimal

@Externalized("account-transfer::#{#this.fromAccountId}")
data class AccountTransferEvent(
    val fromAccountOwnerId: Long,
    val fromAccountId: Long,
    val toAccountId: Long,
    val amount: BigDecimal,
)