package com.pulseai.ledgerone.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NetWorthTrendLine(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    VicoLineChart(
        data = data,
        modifier = modifier
    )
}
