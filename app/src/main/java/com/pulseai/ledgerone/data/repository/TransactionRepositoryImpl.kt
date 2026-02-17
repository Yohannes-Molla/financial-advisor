package com.pulseai.ledgerone.data.repository

import com.pulseai.ledgerone.data.local.dao.TransactionDao
import com.pulseai.ledgerone.data.local.entity.toDomain
import com.pulseai.ledgerone.data.local.entity.toEntity
import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction.toEntity())
    }

    override suspend fun getTransactionByBankId(bankTxId: String): Transaction? {
        return dao.getTransactionByBankId(bankTxId)?.toDomain()
    }

    override fun getTotalBalance(): Flow<Double> {
        return dao.getTotalBalance().map { it ?: 0.0 }
    }
}
