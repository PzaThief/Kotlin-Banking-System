package com.example.bank.account

import com.example.bank.account.application.AccountApplication
import com.example.bank.account.domain.AccountProduct
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
class AccountApplicationTests(
    @Autowired
    val accountApplication: AccountApplication
) {
    @Nested
    inner class CreateAccount {
        @Test
        fun createAccountShouldSameWithSavedAccount() {
            runBlocking {
                val ownerName = "홍길동"
                val initialDeposit = BigDecimal(0)
                val accountProduct = AccountProduct("1111")
                val account = accountApplication.createAccount(ownerName, accountProduct, initialDeposit)
                val savedAccount = accountApplication.getAccount(account.id)
                assertThat(savedAccount)
                    .usingRecursiveComparison()
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal::class.java)
                    .withComparatorForType({a, b-> a.withNano(0).compareTo(b.withNano(0))}, LocalDateTime::class.java)
                    .isEqualTo(account)
            }
        }
    }
}