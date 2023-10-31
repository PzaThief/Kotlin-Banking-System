package com.example.bank.account

import com.example.bank.account.application.AccountApplication
import com.example.bank.account.application.AccountCreateRequest
import com.example.bank.account.application.AccountTransferRequest
import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountProduct
import com.example.bank.account.infrastructure.AccountJpaRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers.returnsFirstArg
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
                    ownerId = 1,
                    initialDeposit = BigDecimal(0),
                    accountProduct = "1111"
                )
                val expectedSavedAccount = Account(
                    id = Account.Id(1),
                    displayId = Account.DisplayId("1111-0000001"),
                    ownerId = accountCreateRequest.ownerId,
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
                assertThat(savedAccount).isEqualTo(account)

                verify(repository, times(1)).saveAndFlush(any<Account>())
                verify(repository, times(1)).findById(any<Account.Id>())
            }
        }
    }

    @Nested
    inner class Transfer {
        @Test
        fun transferShouldKeepTotalAmount() {
            runBlocking {
                val accountOne = Account(
                    id = Account.Id(1),
                    displayId = Account.DisplayId("1111-0000001"),
                    ownerId = 1,
                    accountProduct = AccountProduct.NARASARANG,
                    initialDeposit = BigDecimal(100),
                    balance = BigDecimal(100),
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now()
                )
                val accountTwo = Account(
                    id = Account.Id(2),
                    displayId = Account.DisplayId("1111-0000002"),
                    ownerId = 2,
                    accountProduct = AccountProduct.NARASARANG,
                    initialDeposit = BigDecimal(100),
                    balance = BigDecimal(100),
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now()
                )
                val transferAmount = BigDecimal(50)

                whenever(repository.findById(accountOne.id)).thenReturn(accountOne)
                whenever(repository.findById(accountTwo.id)).thenReturn(accountTwo)
                whenever(repository.saveAndFlush(any<Account>())).then(returnsFirstArg<Account>())

                val accountTransferRequest = AccountTransferRequest(accountTwo.id.value, transferAmount)
                val ok = accountApplication.transfer(accountOne.id.value, accountTransferRequest)
                assert(ok)

                verify(repository, times(2)).saveAndFlush(any<Account>())
                verify(repository, times(2)).findById(any<Account.Id>())
            }
        }

        @Test
        fun transferFailByWrongFromAccountId() {
            runBlocking {
                whenever(repository.findById(Account.Id(1L))).thenReturn(null)

                val accountTransferRequest = AccountTransferRequest(2L, BigDecimal(50))
                assertThrows<Exception> {
                     accountApplication.transfer(1L, accountTransferRequest)
                }
                verify(repository, times(1)).findById(any<Account.Id>())
            }
        }

        @Test
        fun transferFailByWrongToAccountId() {
            runBlocking {
                val accountOne = Account(
                    id = Account.Id(1),
                    displayId = Account.DisplayId("1111-0000001"),
                    ownerId = 1,
                    accountProduct = AccountProduct.NARASARANG,
                    initialDeposit = BigDecimal(100),
                    balance = BigDecimal(100),
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now()
                )
                val transferAmount = BigDecimal(50)

                whenever(repository.findById(accountOne.id)).thenReturn(accountOne)
                whenever(repository.findById(Account.Id(2L))).thenReturn(null)

                val accountTransferRequest = AccountTransferRequest(2L, transferAmount)
                assertThrows<Exception> {
                    accountApplication.transfer(accountOne.id.value, accountTransferRequest)
                }
                verify(repository, times(2)).findById(any<Account.Id>())
            }
        }
    }
}