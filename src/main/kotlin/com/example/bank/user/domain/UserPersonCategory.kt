package com.example.bank.user.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kotlin.String

enum class UserPersonCategory(val code: String) {
    NATURAL("1"), // 자연인
    LEGAL("2") // 법인
    ;

    companion object {
        operator fun invoke(code: String) = UserPersonCategory.values().first { it.code == code }
    }

}


@Converter(autoApply = true)
class UserPersonCategoryConverter : AttributeConverter<UserPersonCategory, String> {
    override fun convertToDatabaseColumn(attribute: UserPersonCategory) = attribute.code
    override fun convertToEntityAttribute(dbData: String) = UserPersonCategory(dbData)
}