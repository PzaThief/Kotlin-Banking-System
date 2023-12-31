package com.example.bank.account.presentation

import com.example.bank.account.application.AccountApplication
import com.example.bank.account.application.AccountCreateRequest
import com.example.bank.account.application.AccountResponse
import com.example.bank.account.application.AccountTransferRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    @Autowired
    val accountApplication: AccountApplication
) {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    suspend fun createAccount(@RequestBody accountCreateRequest: AccountCreateRequest): AccountResponse {
        return accountApplication.createAccount(accountCreateRequest)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getAccount(@PathVariable("id") id: Long): AccountResponse {
        return accountApplication.getAccount(id)
    }

    @PostMapping("/{id}/transfer")
    @ResponseStatus(HttpStatus.OK)
    suspend fun transfer(@PathVariable("id") id: Long, @RequestBody accountTransferRequest: AccountTransferRequest): Boolean {
        return accountApplication.transfer(id, accountTransferRequest)
    }
}