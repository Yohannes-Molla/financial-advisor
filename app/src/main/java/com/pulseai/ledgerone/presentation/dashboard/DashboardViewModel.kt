package com.pulseai.ledgerone.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.domain.model.TransactionType
import com.pulseai.ledgerone.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _totalBalance = MutableStateFlow(0.0)

    val uiState: StateFlow<DashboardUiState> = combine(
        _transactions,
        _totalBalance
    ) { transactions, balance ->
        DashboardUiState.Success(
            transactions = transactions,
            totalBalance = balance
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState.Loading
    )

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase().collect { transactions ->
                val totalBalance = transactions.sumOf {
                    if (it.type == TransactionType.CREDIT) it.amount else -it.amount
                }
                _transactions.value = transactions
                _totalBalance.value = totalBalance
            }
        }
    }
}

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val transactions: List<Transaction>,
        val totalBalance: Double
    ) : DashboardUiState
}
