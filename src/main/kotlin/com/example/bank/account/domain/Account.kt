package com.example.bank.account.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "account", indexes = [
    Index(name = "account_display_id_idx", columnList = "display_id"),
    Index(name = "account_owner_id_idx", columnList = "owner_id"),
])
@EntityListeners(AuditingEntityListener::class)
class Account(
    @EmbeddedId
    val id: Id,
    val displayId: DisplayId,

    @Column(name="owner_id", nullable = false)
    val ownerId: Long,

    @Column(name="account_product_code", nullable = false, updatable = false)
    val accountProduct: AccountProduct,

    @Column(name="initial_deposit", nullable = false, updatable = false)
    val initialDeposit: BigDecimal,

    @Column(name="balance", nullable = false)
    var balance: BigDecimal = initialDeposit,

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "timestamp with time zone", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
) {
    @Embeddable
    data class Id(
        @Column(name="id", nullable = false, unique = true, updatable = false)
        val value: Long
    ): Serializable
    @Converter(autoApply = true)
    class IdConverter: AttributeConverter<Id, Long> {
        override fun convertToDatabaseColumn(attribute: Id): Long = attribute.value
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