package com.example.bank.account

import com.example.bank.account.application.AccountApplication
import com.example.bank.account.application.AccountCreateRequest
import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.AccountJpaRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
class AccountApplicationTests {
    @InjectMocks
    lateinit var accountApplication: AccountApplication

    @Mock
    lateinit var repository: AccountJpaRepository

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
                val expectedSavedAccount = Account(
                    id = Account.Id(1),
                    displayId = Account.DisplayId("1111-0000001"),
                    ownerName = accountCreateRequest.ownerName,
                    accountProduct = AccountProduct(accountCreateRequest.accountProduct),
                    initialDeposit = accountCreateRequest.initialDeposit,
                    balance = accountCreateRequest.initialDeposit,
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now()
                )
                whenever(repository.saveAndFlush(any<Account>())).thenReturn(expectedSavedAccount)
                whenever(repository.findById(any<Account.Id>())).thenReturn(expectedSavedAccount)
                whenever(repository.nextAccountId()).thenReturn(Account.Id(1))


                val account = accountApplication.createAccount(accountCreateRequest)
                val savedAccount = accountApplication.getAccount(account.id)
                assertThat(savedAccount)
                    .usingRecursiveComparison()
                    .withComparatorForType(BigDecimal::compareTo, BigDecimal::class.java)
                    .withComparatorForType(
                        { a, b -> a.withNano(0).compareTo(b.withNano(0)) },
                        LocalDateTime::class.java
                    )
                    .isEqualTo(account)

                verify(repository, times(1)).saveAndFlush(any<Account>())
                verify(repository, times(1)).findById(any<Account.Id>())
            }
        }
    }
}