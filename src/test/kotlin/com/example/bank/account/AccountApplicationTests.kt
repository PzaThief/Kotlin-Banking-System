package com.example.bank.account

import com.example.bank.account.application.AccountApplication
import com.example.bank.account.application.AccountCreateRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class AccountApplicationTests(
    @Autowired
    val accountApplication: AccountApplication
) {
    @Nested
    inner class CreateAccount {
        @Test
        fun createAccountShouldSameWithSavedAccount() {
            runBlocking {
                val accountCreateRequest = AccountCreateRequest(
                    ownerName = "홍길동",
                    initialDeposit = BigDecimal(0),
                    accountProduct = "1111"
                )
                val account = accountApplication.createAccount(accountCreateRequest)
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