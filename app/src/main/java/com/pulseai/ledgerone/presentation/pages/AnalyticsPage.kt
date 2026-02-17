package com.pulseai.ledgerone.presentation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pulseai.ledgerone.presentation.components.ActivityChartWithFilters
import com.pulseai.ledgerone.presentation.components.VicoPieChart
import com.pulseai.ledgerone.presentation.mock.MockDataProvider
import com.pulseai.ledgerone.presentation.mock.Timeframe

@Composable
fun AnalyticsPage() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = "Financial Analytics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Summary Cards
        Row(modifier = Modifier.fillMaxWidth()) {
            AnalyticsSummaryCard(
                label = "Income",
                amount = "ETB 45,000",
                icon = Icons.Outlined.ArrowUpward,
                color = Color(0xFF01D167),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            AnalyticsSummaryCard(
                label = "Expense",
                amount = "ETB 16,460",
                icon = Icons.Outlined.ArrowDownward,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Cash Flow Section
        AnalyticsSection(title = "Cash Flow Trend") {
            ActivityChartWithFilters(
                initialData = MockDataProvider.getNetWorthTrend(Timeframe.WEEKLY),
                onTimeframeChange = { timeframe ->
                    MockDataProvider.getNetWorthTrend(timeframe)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Spending Breakdown Section
        AnalyticsSection(title = "Spending Breakdown") {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(200.dp)) {
                    VicoPieChart(
                        summary = MockDataProvider.getSpendingSummary(),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Legend
                MockDataProvider.getSpendingSummary().categories.forEach { category ->
                    CategoryLegendItem(category.category, category.percentage, Color(category.color))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun AnalyticsSummaryCard(
    label: String,
    amount: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = amount, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AnalyticsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
fun CategoryLegendItem(name: String, percentage: Float, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).padding(2.dp).aspectRatio(1f).padding(2.dp), contentAlignment = Alignment.Center) {
                // Dot
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = color)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            text = "${(percentage * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
