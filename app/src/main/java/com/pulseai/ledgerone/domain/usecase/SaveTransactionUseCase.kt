package com.pulseai.ledgerone.domain.usecase

import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.repository.TransactionRepository
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.saveTransaction(transaction)
    }
}
