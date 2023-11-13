package com.example.bank.user.presentation

import com.example.bank.account.AccountTransferEvent
import com.example.bank.user.application.UserApplication
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Controller


@Controller
class UserListener(
    @Autowired
    val userApplication: UserApplication
) {
    @ApplicationModuleListener
    fun onAccountTransferEvent(event: AccountTransferEvent) {
        runBlocking { userApplication.updateUserBalance(event.fromAccountId, -event.amount) }
    }
}