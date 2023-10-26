package com.example.bank.account

import com.example.bank.account.domain.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.NoSuchElementException

@ActiveProfiles("test")
class AccountDomainTests {
    class NextAccountIdGeneratorImpl : NextAccountIdGenerator {
        var id = 1L
        override fun nextAccountId() = Account.Id(id++)
    }

    @Nested
    inner class CreateAccount {
        @Test
        fun createAccountShouldSameWithGivenInfo() {

            runBlocking {
                val ownerId = 1L
                val initialDeposit = BigDecimal(0)
                val accountProduct = AccountProduct("1111")

                val accountFactory = AccountFactory(NextAccountIdGeneratorImpl())
                val account = accountFactory.createAccount(ownerId, accountProduct, initialDeposit)

                assert(ownerId == account.ownerId)
                assert(initialDeposit == account.initialDeposit)
                assert(initialDeposit == account.balance)
                assert(Account.Id(1) == account.id)
                assert(Account.DisplayId("1111-0000001") == account.displayId)
            }
        }

        @Test
        fun createAccountFailByNonExistsProduct() {
            assertThrows<NoSuchElementException> {
                runBlocking {
                    val ownerId = 1L
                    val initialDeposit = BigDecimal(0)
                    val accountProduct = AccountProduct("9999")

                    val accountFactory = AccountFactory(NextAccountIdGeneratorImpl())
                    accountFactory.createAccount(ownerId, accountProduct, initialDeposit)
                }
            }

        }
    }

    @Nested
    inner class Transfer {
        @Test
        fun transferShouldKeepTotalAmount() {
            runBlocking {
                val accountFactory = AccountFactory(NextAccountIdGeneratorImpl())
                val accountOne = accountFactory.createAccount(1, AccountProduct("1111"), BigDecimal(100))
                val accountTwo = accountFactory.createAccount(2, AccountProduct("1111"), BigDecimal(100))
                assert(accountOne.balance + accountTwo.balance == BigDecimal(200))

                accountOne.transfer(accountTwo, BigDecimal(50))

                assert(accountOne.balance == BigDecimal(100) - BigDecimal(50))
                assert(accountTwo.balance == BigDecimal(100) + BigDecimal(50))
                assert(accountOne.balance + accountTwo.balance == BigDecimal(200))
            }
        }

        // 0원 이체는 가능하다고 가정
        @ParameterizedTest
        @ValueSource(ints = [-10, 150])
        fun transferFailByWrongAmount(transferAmount: Int) {
            runBlocking {
                val accountFactory = AccountFactory(NextAccountIdGeneratorImpl())
                val accountOne = accountFactory.createAccount(1, AccountProduct("1111"), BigDecimal(100))
                val accountTwo = accountFactory.createAccount(2, AccountProduct("1111"), BigDecimal(100))
                assert(accountOne.balance + accountTwo.balance == BigDecimal(200))

                assertThrows<Throwable> {
                    accountOne.transfer(accountTwo, BigDecimal(transferAmount))
                }
            }
        }
    }
}
