package com.example.bank.user

import com.example.bank.user.application.UserApplication
import com.example.bank.user.application.UserCreateRequest
import com.example.bank.user.application.UserResponse
import com.example.bank.user.presentation.UserController
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.time.LocalDateTime

@WebFluxTest(controllers = [UserController::class])
@ActiveProfiles("test")
class UserPresentationTests(
    @Autowired
    val webClient: WebTestClient,
) {
    @MockBean
    lateinit var application: UserApplication

    @Nested
    inner class CreateUser {
        @Test
        fun createAccountShouldReturnDtoWith200() {
            runBlocking {
                val userCreateRequest = UserCreateRequest(
                    loginId = "kotlin",
                    loginPassword = "bank",
                    name = "홍길동",
                    personCategory = "natural",
                    registrationNumber = "990123-1012345",
                )
                val userCreateResponse = UserResponse(
                    id = 1,
                    loginId = "kotlin",
                    name = "홍길동",
                    personCategory = "natural",
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now(),
                )

                whenever(application.createUser(userCreateRequest)).thenReturn(userCreateResponse)
                webClient.post()
                    .uri("/user/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(userCreateRequest))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserResponse::class.java)
                    .consumeWith {
                        Assertions.assertThat(it.responseBody).isNotNull()
                        Assertions.assertThat(it.responseBody!!.id).isNotNull()
                        Assertions.assertThat(it.responseBody!!.createdAt).isNotNull()
                    }

                verify(application, times(1)).createUser(userCreateRequest)
            }
        }
    }
}
