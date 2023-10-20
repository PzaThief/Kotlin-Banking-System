package com.example.bank.account

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountProduct
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class AccountApplicationTests {
    @Nested
    class CreateAccount {
        @Test
        fun createAccountShouldSameWithSavedAccount() {
            val ownerName = "홍길동"
            val initialDeposit = BigDecimal(0)
            val accountProduct = AccountProduct("1111")
            val account = Account.createAccount(ownerName, accountProduct, initialDeposit)
            val savedAccount = Account.getAccount(account.id)
            assert(account == savedAccount)
        }
    }
}