package com.example.bank.account

import com.example.bank.account.domain.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@ActiveProfiles("test")
class AccountDomainTests {

    @Nested
    inner class CreateAccount {
        @Test
        fun createAccountShouldSameWithGivenInfo() {
            class NextAccountIdGeneratorImpl : NextAccountIdGenerator {
                var id = 1L
                override fun nextAccountId() = Account.Id(id++)
            }
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
    }
}
