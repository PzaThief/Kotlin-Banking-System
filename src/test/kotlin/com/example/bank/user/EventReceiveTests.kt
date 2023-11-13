package com.example.bank.user

import com.example.bank.account.AccountTransferEvent
import com.example.bank.user.domain.User
import com.example.bank.user.domain.UserPersonCategory
import com.example.bank.user.infrastructure.UserJpaRepository
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.modulith.test.*
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal


@ExtendWith(MockitoExtension::class)
@ApplicationModuleTest
@ActiveProfiles("test")
class EventReceiveTests {

    @MockBean
    lateinit var repository: UserJpaRepository

    @Test
    fun accountTransferShouldChangeUserBalance(scenario: Scenario) {
        runBlocking {
            var userRecord = User(
                User.Id(1), "test", "testPassword", "홍길동", UserPersonCategory.NATURAL, "990123-1012345", BigDecimal(100)
            )
            whenever(repository.findById(User.Id(1))).thenReturn(userRecord)
            whenever(repository.saveAndFlush(any<User>())).then {
                userRecord = it.arguments.first() as User
                userRecord
            }

            val oldAmount = userRecord.totalBalance.add(BigDecimal.ZERO)
            assert(oldAmount !== userRecord.totalBalance)

            val changeAmount = BigDecimal(50)
            val event = AccountTransferEvent(
                fromAccountOwnerId = userRecord.id.value,
                fromAccountId = 1,
                toAccountId = 2,
                amount = changeAmount,
            )
            scenario.publish(event)
                .andWaitForStateChange { repository.findById(User.Id(1)) }
                .andVerify { result ->
                    result!!
                    assertThat(result.lastActivity).isNotNull()
                    assertThat(result.totalBalance).isEqualTo(oldAmount - changeAmount)
                }
        }
    }
}