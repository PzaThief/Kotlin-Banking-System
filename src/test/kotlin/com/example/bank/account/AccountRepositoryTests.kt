package com.example.bank.account

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.jpa.AccountJpaRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class AccountRepositoryTests(
    @Autowired
    private val accountRepository: AccountJpaRepository
) {

    @Test
    fun nextAccountIdShouldIncrease() {
        val firstAccountId = accountRepository.nextAccountId()
        val secondAccountId = accountRepository.nextAccountId()
        assert(firstAccountId.value < secondAccountId.value)
    }

    @Test
    fun jpaAuditShouldGenerateCreatedAtAndUpdatedAt() {
        val account = accountRepository.saveAndFlush(
            Account(
                id = Account.Id(1),
                displayId = Account.DisplayId("1111-0000001"),
                ownerName = "홍길동",
                accountProduct = AccountProduct("1111"),
                initialDeposit = BigDecimal(0),
            )
        )
        assertNotNull(account.createdAt)

        account.balance = BigDecimal(100)
        accountRepository.saveAndFlush(account)
        assertNotNull(account.updatedAt)
    }
}