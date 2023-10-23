package com.example.bank.account.application

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountFactory
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.jpa.AccountJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class AccountApplication(
    @Autowired
    val accountRepository: AccountJpaRepository
) {

    val accountFactory = AccountFactory(accountRepository)
    suspend fun createAccount(ownerName: String, accountProduct: AccountProduct, initialDeposit:BigDecimal): Account {
        val account = accountFactory.createAccount(ownerName, accountProduct, initialDeposit)
        return accountRepository.saveAndFlush(account)
    }

    suspend fun getAccount(accountId: Account.Id): Account {
        return accountRepository.findById(accountId)
    }

}