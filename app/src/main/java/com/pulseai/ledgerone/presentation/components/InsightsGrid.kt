package com.pulseai.ledgerone.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pulseai.ledgerone.domain.model.UpcomingBill
import com.pulseai.ledgerone.presentation.mock.MockDataProvider

import com.pulseai.ledgerone.presentation.mock.PlannerItem

@Composable
fun InsightsGrid(
    upcomingBills: List<UpcomingBill>,
    plannerItems: List<PlannerItem>,
    modifier: Modifier = Modifier
) {
    val cashFlowModel = MockDataProvider.getCashFlowData()

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Upcoming & Goals",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )

        // Upcoming Bills
        BentoCard(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            UpcomingBillsCard(bills = upcomingBills, modifier = Modifier.fillMaxWidth())
        }

        // Planned Goals Preview
        if (plannerItems.isNotEmpty()) {
            Text(
                text = "Focused Goals",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                plannerItems.take(2).forEach { item ->
                    BentoCard(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Target: ${item.targetDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val progress = (item.actualAmount / item.plannedAmount).toFloat().coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(androidx.compose.foundation.shape.CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            )
                        }
                    }
                }
            }
        }
        
        BentoCard(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Cash Flow",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Monthly Income vs Expense",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                VicoBarChart(
                    incomeData = cashFlowModel.first,
                    expenseData = cashFlowModel.second,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.animateContentSize(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}
