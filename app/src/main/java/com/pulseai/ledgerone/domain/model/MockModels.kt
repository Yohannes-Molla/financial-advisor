package com.pulseai.ledgerone.domain.model

data class UpcomingBill(
    val id: String,
    val merchantName: String,
    val amount: Double,
    val dueDate: String,
    val category: String
)

data class SpendingSummary(
    val categories: List<CategorySpend>
)

data class CategorySpend(
    val category: String,
    val amount: Double,
    val percentage: Float,
    val color: Long // Hex color for the pie chart
)
