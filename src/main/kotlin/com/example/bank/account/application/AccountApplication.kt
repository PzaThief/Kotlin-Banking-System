package com.example.bank.account.application

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountFactory
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.AccountJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AccountApplication(
    @Autowired
    val accountRepository: AccountJpaRepository
) {
    val accountFactory = AccountFactory(accountRepository)
    suspend fun createAccount(accountCreateRequest: AccountCreateRequest): AccountResponse {
        return accountFactory.createAccount(
            accountCreateRequest.ownerId,
            AccountProduct(accountCreateRequest.accountProduct),
            accountCreateRequest.initialDeposit
        )
            .let { accountRepository.saveAndFlush(it) }
            .let { AccountResponse(it) }
    }

    suspend fun getAccount(accountId: Long): AccountResponse {
        return accountRepository.findById(Account.Id(accountId))
            .let { AccountResponse(it) }
    }

    suspend fun transfer(accountTransferRequest: AccountTransferRequest): Boolean {
        val fromAccount = accountRepository.findById(Account.Id(accountTransferRequest.fromAccountId))
        val toAccount = accountRepository.findById(Account.Id(accountTransferRequest.toAccountId))

        val ok = runCatching {
            fromAccount.transfer(toAccount, accountTransferRequest.amount)
            accountRepository.saveAndFlush(fromAccount)
            accountRepository.saveAndFlush(toAccount)
        }.isSuccess

        return ok
    }

}