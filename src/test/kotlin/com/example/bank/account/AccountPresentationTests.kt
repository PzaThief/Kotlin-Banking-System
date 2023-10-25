package com.example.bank.account

import com.example.bank.account.domain.Account
import com.example.bank.account.domain.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters


@WebFluxTest(controllers = [AccountController::class])
@ActiveProfiles("test")
class AccountPresentationTests(
    @MockBean
    val repository: AccountRepository,
    @Autowired
    val webClient: WebTestClient
) {
    @Nested
    inner class CreateAccount {
        @Test
        fun createAccountShouldReturnDtoWith200() {
            Mockito.`when`(repository.save(Mockito.any(Account::class.java))).then(AdditionalAnswers.returnsFirstArg<Account>())

            val accountCreateRequest = AccountCreateRequest(ownerName, accountProduct, initialDeposit)
            val accountCreateResponse = AccountCreateResponse(ownerName, accountProduct, initialDeposit)
            webClient.post()
                .uri("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(accountCreateRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountCreateResponse::class.java)
                .consumeWith {
                    assertThat(it).isEqualTo(accountCreateResponse)
                }

            Mockito.verify(repository, times(1)).save(Mockito.any(Account::class.java)))
        }
    }
}