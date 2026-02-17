package com.pulseai.ledgerone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pulseai.ledgerone.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE transactionId = :bankTxId LIMIT 1")
    suspend fun getTransactionByBankId(bankTxId: String): TransactionEntity?

    @Query("SELECT SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE -amount END) FROM transactions")
    fun getTotalBalance(): Flow<Double?>
}
