package com.example.bank.user.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(
    name = "`user`", indexes = [
        Index(name = "user_login_id_idx", columnList = "login_id")
    ]
)
@EntityListeners(AuditingEntityListener::class)
class User(
    @EmbeddedId
    val id: Id,

    @Column(name = "login_id", nullable = false, unique = true)
    val loginId: String,

    @Column(name = "login_password", nullable = false)
    var loginPassword: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "person_category", nullable = false)
    val personCategory: UserPersonCategory,

    @Column(name = "registration_number", nullable = false)
    val registrationNumber: String,

    @Column(name = "total_balance", nullable = false)
    var totalBalance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "last_activity", columnDefinition = "timestamp with time zone")
    var lastActivity: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
    var updatedAt: LocalDateTime? = null,

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "timestamp with time zone", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
) {

    fun changeBalance(amount:BigDecimal) {
        this.lastActivity = LocalDateTime.now()
        this.totalBalance += amount
    }

    @Embeddable
    data class Id(
        @Column(name = "id", nullable = false, unique = true, updatable = false)
        val value: Long
    ) : Serializable

    @Converter(autoApply = true)
    class IdConverter : AttributeConverter<Id, Long> {
        override fun convertToDatabaseColumn(attribute: Id): Long = attribute.value
        override fun convertToEntityAttribute(dbData: Long) = Id(dbData)

    }
}