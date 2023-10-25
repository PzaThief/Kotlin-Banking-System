package com.example.bank.user

import com.example.bank.user.application.UserApplication
import com.example.bank.user.application.UserCreateRequest
import com.example.bank.user.domain.User
import com.example.bank.user.domain.UserPersonCategory
import com.example.bank.user.infrastructure.UserJpaRepository
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
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
class UserApplicationTests {
    @InjectMocks
    lateinit var userApplication: UserApplication

    @Mock
    lateinit var repository: UserJpaRepository

    @Nested
    inner class CreateUser {
        @Test
        fun createUserShouldSameWithSavedUser() {
            runBlocking {
                val userCreateRequest = UserCreateRequest(
                    loginId = "kotlin",
                    loginPassword = "bank",
                    name = "홍길동",
                    personCategory = "1",
                    registrationNumber = "990123-1012345",
                )
                val expectedSavedUser = User(
                    id = User.Id(1),
                    loginId = "kotlin",
                    loginPassword = "hashedString",
                    name = "홍길동",
                    personCategory = UserPersonCategory("1"),
                    registrationNumber = "encryptedString",
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now()
                )
                whenever(repository.saveAndFlush(any<User>())).thenReturn(expectedSavedUser)
                whenever(repository.findById(any<User.Id>())).thenReturn(expectedSavedUser)
                whenever(repository.nextUserId()).thenReturn(User.Id(1))

                val user = userApplication.createUser(userCreateRequest)
                val savedUser = userApplication.getUser(user.id)
                assertThat(savedUser)
                    .usingRecursiveComparison()
                    .withComparatorForType(
                        { a, b -> a.withNano(0).compareTo(b.withNano(0)) },
                        LocalDateTime::class.java
                    )
                    .isEqualTo(user)

                verify(repository, times(1)).saveAndFlush(any<User>())
                verify(repository, times(1)).findById(any<User.Id>())
            }
        }
    }
}