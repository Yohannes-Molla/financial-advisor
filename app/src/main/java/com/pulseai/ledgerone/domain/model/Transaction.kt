package com.pulseai.ledgerone.domain.model

import java.util.Date

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val bank: BankType,
    val merchant: String, // Or Sender/Receiver
    val timestamp: Long,
    val transactionId: String, // Bank's Tx ID
    val verificationStatus: VerificationStatus = VerificationStatus.DETECTED
)

enum class VerificationStatus {
    DETECTED,
    VERIFIED,
    MANUAL,
    FLAGGED
}
