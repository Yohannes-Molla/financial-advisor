package com.pulseai.ledgerone.domain.usecase

import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getAllTransactions()
    }
}
