package com.pulseai.ledgerone.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pulseai.ledgerone.domain.model.Transaction
import com.pulseai.ledgerone.presentation.components.*
import com.pulseai.ledgerone.presentation.mock.MockDataProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val transactions = remember { MockDataProvider.getTransactions() }
    val plannerItems = remember { MockDataProvider.getPlannerItems() }
    val upcomingBills = remember { MockDataProvider.getUpcomingBills() }

    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            DashboardExtendedFab(
                onClick = { /* New Transaction */ },
                expanded = true
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    SpendableHeader(
                        spendableAmount = 28540.0,
                        totalCash = 45000.0,
                        billsRemaining = 16460.0,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                
                item {
                    Text(
                        text = "Your Accounts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
                    )
                    BankAccountCarousel(modifier = Modifier.padding(bottom = 16.dp))
                }
                
                item {
                    InsightsGrid(
                        upcomingBills = upcomingBills,
                        plannerItems = plannerItems
                    )
                }
                
                item {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp)
                    )
                    TransactionSearchBar(modifier = Modifier.padding(bottom = 8.dp))
                    TransactionFilters()
                }
                
                itemsIndexed(transactions) { index, transaction ->
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(600, delayMillis = index * 50)) + 
                                slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(600, delayMillis = index * 50)
                                )
                    ) {
                        TransactionItem(
                            transaction = transaction,
                            modifier = Modifier.clickable {
                                selectedTransaction = transaction
                                showSheet = true
                            }
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                }
            }

            if (showSheet) {
                TransactionDetailBottomSheet(
                    transaction = selectedTransaction,
                    onDismiss = { showSheet = false },
                    sheetState = sheetState
                )
            }
        }
    }
}

