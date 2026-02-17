package com.pulseai.ledgerone.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.pulseai.ledgerone.presentation.mock.Timeframe

@Composable
fun VicoLineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    val modelProducer = remember { ChartEntryModelProducer() }
    
    LaunchedEffect(data) {
        modelProducer.setEntries(data.mapIndexed { index, value -> entryOf(index, value) })
    }

    Chart(
        chart = lineChart(
            lines = listOf(
                LineChart.LineSpec(
                    lineColor = lineColor.toArgb(),
                    lineBackgroundShader = null 
                )
            )
        ),
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(
            label = null,
            guideline = null,
            tick = null
        ),
        bottomAxis = rememberBottomAxis(
            label = null,
            guideline = null,
            tick = null
        ),
        modifier = modifier.fillMaxSize(),
        chartScrollState = rememberChartScrollState()
    )
}

@Composable
fun ActivityChartWithFilters(
    initialData: List<Float>,
    onTimeframeChange: (Timeframe) -> List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    var selectedTimeframe by remember { mutableStateOf(Timeframe.WEEKLY) }
    var chartData by remember { mutableStateOf(initialData) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Timeframe.entries.forEach { timeframe ->
                TextButton(
                    onClick = {
                        selectedTimeframe = timeframe
                        chartData = onTimeframeChange(timeframe)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (selectedTimeframe == timeframe) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = timeframe.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selectedTimeframe == timeframe) FontWeight.Bold else FontWeight.Light
                    )
                }
            }
        }
        
        Box(modifier = Modifier.height(120.dp)) {
            VicoLineChart(data = chartData, lineColor = lineColor)
        }
    }
}
