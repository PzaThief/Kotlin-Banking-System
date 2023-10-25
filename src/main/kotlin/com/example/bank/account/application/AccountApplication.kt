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

}