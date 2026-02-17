package com.pulseai.ledgerone.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun VicoBarChart(
    incomeData: List<Float>,
    expenseData: List<Float>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { ChartEntryModelProducer() }
    
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)

    LaunchedEffect(incomeData, expenseData) {
        val incomeEntries = incomeData.mapIndexed { index, value -> entryOf(index, value) }
        val expenseEntries = expenseData.mapIndexed { index, value -> entryOf(index, value) }
        modelProducer.setEntries(incomeEntries, expenseEntries)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChartLegendItem("Income", primaryColor)
            ChartLegendItem("Expense", secondaryColor)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Chart(
            chart = columnChart(
                columns = listOf(
                    lineComponent(
                        color = primaryColor,
                        thickness = 8.dp,
                        shape = Shapes.rectShape
                    ),
                    lineComponent(
                        color = secondaryColor,
                        thickness = 8.dp,
                        shape = Shapes.rectShape
                    )
                ),
                spacing = 12.dp,
                innerSpacing = 4.dp
            ),
            chartModelProducer = modelProducer,
            startAxis = rememberStartAxis(
                valueFormatter = { value, _ -> "${value.toInt() / 1000}k" }
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ -> 
                    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                    months.getOrNull(value.toInt() % 12) ?: ""
                }
            ),
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )
    }
}

@Composable
private fun ChartLegendItem(label: String, color: Color) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
