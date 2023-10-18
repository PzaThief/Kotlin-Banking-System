package com.example.bank.domain.account

enum class AccountProduct(val code: String) {
    NARASARANG("1111")
    ;

    companion object {
        operator fun invoke(code: String) = AccountProduct.values().first { it.code == code }
    }

}