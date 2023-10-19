package com.example.bank.account.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class AccountProduct(val code: String) {
    NARASARANG("1111")
    ;
    companion object {
        operator fun invoke(code: String) = AccountProduct.values().first { it.code == code }
    }
}

@Converter(autoApply = true)
class AccountProductConverter: AttributeConverter<AccountProduct, String> {
    override fun convertToDatabaseColumn(attribute: AccountProduct) = attribute.code
    override fun convertToEntityAttribute(dbData: String) = AccountProduct(dbData)
}