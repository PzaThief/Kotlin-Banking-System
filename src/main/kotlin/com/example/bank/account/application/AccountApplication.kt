package com.example.bank.account.application

import com.example.bank.account.AccountTransferEvent
import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountFactory
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.AccountJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class AccountApplication(
    @Autowired
    val accountRepository: AccountJpaRepository,
    @Autowired
    val eventPublisher: ApplicationEventPublisher
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
        val account = accountRepository.findById(Account.Id(accountId))
            ?: throw Exception("cannot find account by id $accountId")

        return AccountResponse(account)
    }

    suspend fun transfer(fromAccountId: Long, accountTransferRequest: AccountTransferRequest): Boolean {
        val fromAccount = accountRepository.findById(Account.Id(fromAccountId))
            ?: throw Exception("cannot find source account by id $fromAccountId")
        val toAccount = accountRepository.findById(Account.Id(accountTransferRequest.toAccountId))
            ?: throw Exception("cannot find destination account by id ${accountTransferRequest.toAccountId}")
        fromAccount.transfer(toAccount, accountTransferRequest.amount)
        accountRepository.saveAndFlush(fromAccount)
        accountRepository.saveAndFlush(toAccount)

        eventPublisher.publishEvent(
            AccountTransferEvent(
                fromAccount.ownerId,
                fromAccount.id.value,
                toAccount.id.value,
                accountTransferRequest.amount
            )
        )

        return true
    }

}