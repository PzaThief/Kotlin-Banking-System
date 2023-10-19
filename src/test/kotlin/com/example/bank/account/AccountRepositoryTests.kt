package com.example.bank.account

import com.example.bank.account.domain.AccountRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AccountRepositoryTests(
    @Autowired
    private val accountRepository: AccountRepository
) {

    @Test
    fun nextAccountId() {
        val firstAccountId = accountRepository.nextAccountId()
        val secondAccountId = accountRepository.nextAccountId()
        assertNotNull(firstAccountId)
        assertNotNull(secondAccountId)
        assertTrue(firstAccountId.value.toInt() < secondAccountId.value.toInt())
    }
}