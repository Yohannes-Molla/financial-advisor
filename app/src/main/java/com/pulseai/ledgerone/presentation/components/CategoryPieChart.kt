package com.pulseai.ledgerone.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.pulseai.ledgerone.domain.model.SpendingSummary

@Composable
fun CategoryPieChart(
    summary: SpendingSummary,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(100.dp)) {
            var startAngle = -90f
            summary.categories.forEach { category ->
                val sweepAngle = category.percentage * 360f
                drawArc(
                    color = Color(category.color),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 24f, cap = StrokeCap.Round)
                )
                startAngle += sweepAngle
            }
        }
    }
}
