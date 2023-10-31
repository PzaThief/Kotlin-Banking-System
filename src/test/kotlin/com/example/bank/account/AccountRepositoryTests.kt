package com.example.bank.account

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.AccountJpaRepository
import com.example.bank.config.JpaAuditingConfig
import org.junit.jupiter.api.Assertions.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@DataJpaTest
@Import(JpaAuditingConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
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
                ownerId = 1,
                accountProduct = AccountProduct("1111"),
                initialDeposit = BigDecimal(0),
            )
        )
        assertNotNull(account.createdAt)

        val lastUpdatedAt = account.updatedAt
        account.balance = BigDecimal(100)
        accountRepository.saveAndFlush(account)
        assert(account.updatedAt!! > lastUpdatedAt)
    }

    @Test
    fun findByIdShouldReturnSavedAccount() {
        val savedAccount = accountRepository.saveAndFlush(
            Account(
                id = Account.Id(1),
                displayId = Account.DisplayId("1111-0000001"),
                ownerId = 1,
                accountProduct = AccountProduct("1111"),
                initialDeposit = BigDecimal(0),
            )
        )

        val account = accountRepository.findById(savedAccount.id)
        assertThat(account).isEqualTo(savedAccount)
    }

    @Test
    fun findByIdFailByWrongId() {
        val account = accountRepository.findById(Account.Id(99))
        assertThat(account).isNull()
    }
}