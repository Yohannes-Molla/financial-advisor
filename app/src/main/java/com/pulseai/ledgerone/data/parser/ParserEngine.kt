package com.pulseai.ledgerone.data.parser

import com.pulseai.ledgerone.domain.model.BankType
import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.model.TransactionType
import com.pulseai.ledgerone.domain.model.VerificationStatus
import java.util.Date
import java.util.regex.Pattern
import javax.inject.Inject

class ParserEngine @Inject constructor() {

    fun parse(sender: String, body: String): Transaction? {
        return when {
            sender.contains("CBE", ignoreCase = true) || body.contains("CBE", ignoreCase = true) -> parseCBE(body)
            sender.contains("telebirr", ignoreCase = true) || body.contains("telebirr", ignoreCase = true) -> parseTelebirr(body)
            sender.contains("Boa", ignoreCase = true) || body.contains("Bank of Abyssinia", ignoreCase = true) -> parseBoa(body)
            else -> null
        }
    }

    private fun parseCBE(body: String): Transaction? {
        // Pattern: "credited with ETB 5,000.00" or "debited with ETB 100.00"
        val amountPattern = Pattern.compile("ETB\\s+([\\d,]+\\.\\d{2})")
        val amountMatcher = amountPattern.matcher(body)
        
        if (!amountMatcher.find()) return null
        
        val amountStr = amountMatcher.group(1)?.replace(",", "") ?: return null
        val amount = amountStr.toDoubleOrNull() ?: return null

        val type = if (body.contains("credited", ignoreCase = true)) TransactionType.CREDIT else TransactionType.DEBIT
        
        // Extract Ref ID
        val refPattern = Pattern.compile("Ref:\\s*(\\w+)")
        val refMatcher = refPattern.matcher(body)
        val txId = if (refMatcher.find()) refMatcher.group(1) ?: System.currentTimeMillis().toString() else System.currentTimeMillis().toString()

        return Transaction(
            amount = amount,
            type = type,
            bank = BankType.CBE,
            merchant = "CBE Transaction", // Placeholder, improve with extraction
            timestamp = System.currentTimeMillis(), // Ideally parse date from SMS
            transactionId = txId,
            verificationStatus = VerificationStatus.DETECTED
        )
    }

    private fun parseTelebirr(body: String): Transaction? {
        // Pattern: "received ETB 100.00 from" or "transferred ETB 50.00 to"
        val amountPattern = Pattern.compile("ETB\\s+([\\d,]+\\.\\d{2})")
        val amountMatcher = amountPattern.matcher(body)
        
        if (!amountMatcher.find()) return null
        
        val amountStr = amountMatcher.group(1)?.replace(",", "") ?: return null
        val amount = amountStr.toDoubleOrNull() ?: return null

        val type = if (body.contains("received", ignoreCase = true)) TransactionType.CREDIT else TransactionType.DEBIT

        val idPattern = Pattern.compile("Transaction ID:\\s*(\\w+)")
        val idMatcher = idPattern.matcher(body)
        val txId = if (idMatcher.find()) idMatcher.group(1) ?: System.currentTimeMillis().toString() else System.currentTimeMillis().toString()

        return Transaction(
            amount = amount,
            type = type,
            bank = BankType.TELEBIRR,
            merchant = "Telebirr Transaction",
            timestamp = System.currentTimeMillis(),
            transactionId = txId,
            verificationStatus = VerificationStatus.DETECTED
        )
    }
    
    private fun parseBoa(body: String): Transaction? {
        // Placeholder for BOA logic
        return null 
    }
}
