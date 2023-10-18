package com.example.bank.domain.account

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class AccountTests {

    @Nested
    class CreateAccount() {
        @Test
        fun success() {
            val ownerName = "홍길동"
            val initialDeposit = BigDecimal(0)
            val accountProduct = AccountProduct("1111")

            val account = Account(
                ownerName = ownerName,
                accountProduct = accountProduct,
                initialDeposit = initialDeposit,
            )

            assertEquals(initialDeposit, account.balance)
            assertEquals("${accountProduct.code}-0000001", account.displayId())
        }
    }
}
