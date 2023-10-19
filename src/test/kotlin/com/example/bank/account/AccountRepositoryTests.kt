package com.example.bank.account

import com.example.bank.account.infrastructure.jpa.AccountJpaRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AccountRepositoryTests(
    @Autowired
    private val accountRepository: AccountJpaRepository
) {

    @Test
    fun nextAccountId() {
        val firstAccountId = accountRepository.nextAccountId().also { println(it) }
        val secondAccountId = accountRepository.nextAccountId().also { println(it) }
        assert(firstAccountId.value < secondAccountId.value)
    }
}