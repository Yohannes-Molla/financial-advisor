package com.pulseai.ledgerone.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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

    val scrollState = rememberLazyListState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            DashboardExtendedFab(
                onClick = { /* New Transaction */ },
                expanded = scrollState.firstVisibleItemIndex == 0
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    ScrollAnimatedSection(scrollState, 0) {
                        SpendableHeader(
                            spendableAmount = 28540.0,
                            totalCash = 45000.0,
                            billsRemaining = 16460.0,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ScrollAnimatedSection(scrollState, 1) {
                        Column {
                            Text(
                                text = "Your Accounts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
                            )
                            BankAccountCarousel(modifier = Modifier.padding(bottom = 16.dp))
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ScrollAnimatedSection(scrollState, 2) {
                        InsightsGrid(
                            upcomingBills = upcomingBills,
                            plannerItems = plannerItems
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ScrollAnimatedSection(scrollState, 3) {
                        Column {
                            Text(
                                text = "Transactions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
                            )
                            TransactionSearchBar(modifier = Modifier.padding(bottom = 8.dp))
                            TransactionFilters()
                        }
                    }
                }
                
                itemsIndexed(transactions) { index, transaction ->
                    val itemIndex = 4 + index
                    ScrollAnimatedSection(scrollState, itemIndex) {
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
                    Spacer(modifier = Modifier.height(100.dp)) // Space for FAB
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


@Composable
fun ScrollAnimatedSection(
    state: LazyListState,
    index: Int,
    content: @Composable () -> Unit
) {
    val alpha = remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            val itemInfo = visibleItemsInfo.firstOrNull { it.index == index }

            if (itemInfo == null) {
                0f
            } else {
                val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                val itemTop = itemInfo.offset
                val itemBottom = itemInfo.offset + itemInfo.size
                
                // Calculate how much of the item is in view
                val visibilityThreshold = viewportHeight * 0.15f
                val progress = when {
                    itemTop > viewportHeight -> 0f
                    itemBottom < 0 -> 0f
                    itemTop > viewportHeight - visibilityThreshold -> {
                        (viewportHeight - itemTop) / visibilityThreshold
                    }
                    itemBottom < visibilityThreshold -> {
                        itemBottom / visibilityThreshold
                    }
                    else -> 1f
                }
                progress.coerceIn(0f, 1f)
            }
        }
    }

    val scale = remember {
        derivedStateOf {
            0.92f + (0.08f * alpha.value)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha.value
                this.scaleX = scale.value
                this.scaleY = scale.value
            }
    ) {
        content()
    }
}
