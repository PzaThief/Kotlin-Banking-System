package com.example.bank.account.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "account", indexes = [
    Index(name = "account_display_id_idx", columnList = "display_id")
])
class Account(
    @EmbeddedId
    val id: Id,
    val displayId: DisplayId,

    @Column(name="owner_name", nullable = false)
    val ownerName: String,

    @Column(name="account_product_code", nullable = false, updatable = false)
    val accountProduct: AccountProduct,

    @Column(name="initial_deposit", nullable = false, updatable = false)
    val initialDeposit: BigDecimal,

    @Column(name="balance", nullable = false)
    var balance: BigDecimal = initialDeposit,

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
    var updatedAt: LocalDateTime? = null,

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "timestamp with time zone", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null
) {
    @Embeddable
    data class Id(
        @Column(name="id", nullable = false, unique = true, updatable = false)
        val value: Long
    ): Serializable
    @Converter(autoApply = true)
    class IdConverter: AttributeConverter<Id, Long> {
        override fun convertToDatabaseColumn(attribute: Id): Long = attribute.value.toLong()
        override fun convertToEntityAttribute(dbData: Long) = Id(dbData)

    }

    @Embeddable
    data class DisplayId(
        @Column(name="display_id", nullable = false, unique = true, updatable = false)
        val value: String)
    @Converter(autoApply = true)
    class DisplayIdConverter: AttributeConverter<DisplayId, String> {
        override fun convertToDatabaseColumn(attribute: DisplayId?) = attribute.toString()
        override fun convertToEntityAttribute(dbData: String) = DisplayId(dbData)
    }
}