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
    class CreateAccount {
        @Test
        fun success() {
            class NextAccountIdGeneratorImpl:NextAccountIdGenerator {
                var id = 1L
                override fun nextAccountId() = Account.Id(id++)
            }
            runBlocking {
                val ownerName = "홍길동"
                val initialDeposit = BigDecimal(0)
                val accountProduct = AccountProduct("1111")

                val accountFactory = AccountFactory(NextAccountIdGeneratorImpl())
                val account = accountFactory.createAccount(ownerName, accountProduct, initialDeposit)

                assert(initialDeposit == account.balance)
                assert(Account.DisplayId("1111-0000001") == account.displayId)
            }
        }
    }
}
