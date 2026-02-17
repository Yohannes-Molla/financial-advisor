package com.pulseai.ledgerone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pulseai.ledgerone.domain.model.BankType
import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.model.TransactionType
import com.pulseai.ledgerone.domain.model.VerificationStatus

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val bank: BankType,
    val merchant: String,
    val timestamp: Long,
    val transactionId: String,
    val verificationStatus: VerificationStatus
)

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        type = type,
        bank = bank,
        merchant = merchant,
        timestamp = timestamp,
        transactionId = transactionId,
        verificationStatus = verificationStatus
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        type = type,
        bank = bank,
        merchant = merchant,
        timestamp = timestamp,
        transactionId = transactionId,
        verificationStatus = verificationStatus
    )
}
