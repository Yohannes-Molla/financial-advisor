package com.pulseai.ledgerone.domain.repository

import com.pulseai.ledgerone.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun getTransactionByBankId(bankTxId: String): Transaction?
    fun getTotalBalance(): Flow<Double>
}
