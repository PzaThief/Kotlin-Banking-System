package com.example.bank.account

import com.example.bank.account.application.AccountApplication
import com.example.bank.account.application.AccountCreateRequest
import com.example.bank.account.application.AccountResponse
import com.example.bank.account.presentation.AccountController
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.math.BigDecimal
import java.time.LocalDateTime


@WebFluxTest(controllers = [AccountController::class])
@ActiveProfiles("test")
class AccountPresentationTests(
    @Autowired
    val webClient: WebTestClient,
) {
    @MockBean
    lateinit var application: AccountApplication

    @Nested
    inner class CreateAccount {
        @Test
        fun createAccountShouldReturnDtoWith200() {
            runBlocking {
                val accountCreateRequest = AccountCreateRequest(
                    ownerName = "홍길동",
                    accountProduct = "1111",
                    initialDeposit = BigDecimal(100)
                )
                val accountCreateResponse = AccountResponse(
                    id = 1L,
                    displayId = "1111-0000001",
                    ownerName = "홍길동",
                    accountProduct = "1111",
                    initialDeposit = BigDecimal(100),
                    balance = BigDecimal(100),
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now(),
                )

                Mockito.`when`(application.createAccount(accountCreateRequest)).thenReturn(accountCreateResponse)
                webClient.post()
                    .uri("/account/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(accountCreateRequest))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(AccountResponse::class.java)
                    .consumeWith {
                        assertThat(it.responseBody).isNotNull()
                        assertThat(it.responseBody!!.id).isNotNull()
                        assertThat(it.responseBody!!.createdAt).isNotNull()
                    }

                Mockito.verify(application, times(1)).createAccount(accountCreateRequest)
            }
        }

        @Test
        fun getAccountShouldReturnDtoWith200() {
            runBlocking {
                val accountCreateResponse = AccountResponse(
                    id = 1L,
                    displayId = "1111-0000001",
                    ownerName = "홍길동",
                    accountProduct = "1111",
                    initialDeposit = BigDecimal(100),
                    balance = BigDecimal(100),
                    updatedAt = LocalDateTime.now(),
                    createdAt = LocalDateTime.now(),
                )

                Mockito.`when`(application.getAccount(1L)).thenReturn(accountCreateResponse)
                webClient.get()
                    .uri("/account/${1L}")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(AccountResponse::class.java)
                    .consumeWith {
                        assertThat(it.responseBody).isNotNull()
                        assertThat(it.responseBody!!.id).isNotNull()
                        assertThat(it.responseBody!!.createdAt).isNotNull()
                    }

                Mockito.verify(application, times(1)).getAccount(1L)
            }
        }
    }
}