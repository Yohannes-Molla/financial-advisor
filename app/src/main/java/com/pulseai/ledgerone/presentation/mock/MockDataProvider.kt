package com.pulseai.ledgerone.presentation.mock

import com.pulseai.ledgerone.domain.model.*

enum class Timeframe {
    WEEKLY, MONTHLY, YEARLY
}

object MockDataProvider {
    fun getTransactions(): List<Transaction> = listOf(
        Transaction(1L, 450.0, TransactionType.DEBIT, BankType.TELEBIRR, "Telebirr - Zmall", System.currentTimeMillis(), "TX001"),
        Transaction(2L, 12000.0, TransactionType.DEBIT, BankType.CBE, "CBE - Rent", System.currentTimeMillis() - 86400000, "TX002"),
        Transaction(3L, 2500.0, TransactionType.DEBIT, BankType.UNKNOWN, "Awash - Gas", System.currentTimeMillis() - 172800000, "TX003"),
        Transaction(4L, 45000.0, TransactionType.CREDIT, BankType.CBE, "Salary Deposit", System.currentTimeMillis() - 86400000 * 15, "TX004"),
        Transaction(5L, 1200.50, TransactionType.DEBIT, BankType.TELEBIRR, "Grocery Store", System.currentTimeMillis() - 3600000, "TX005"),
        Transaction(6L, 850.0, TransactionType.DEBIT, BankType.CBE, "Electricity Bill", System.currentTimeMillis() - 86400000 * 5, "TX006"),
        Transaction(7L, 1500.0, TransactionType.DEBIT, BankType.TELEBIRR, "Internet Subscription", System.currentTimeMillis() - 86400000 * 7, "TX007"),
        Transaction(8L, 5000.0, TransactionType.DEBIT, BankType.CBE, "Transfer to Mom", System.currentTimeMillis() - 86400000 * 9, "TX008"),
        Transaction(9L, 150.0, TransactionType.DEBIT, BankType.UNKNOWN, "Coffee Shop", System.currentTimeMillis() - 7200000, "TX009"),
        Transaction(10L, 890.0, TransactionType.DEBIT, BankType.TELEBIRR, "Zmall Delivery", System.currentTimeMillis() - 14400000, "TX010")
    )

    fun getSpendingSummary(): SpendingSummary = SpendingSummary(
        categories = listOf(
            CategorySpend("Food", 4500.0, 0.45f, 0xFF0056D2),      // Azure Primary
            CategorySpend("Transport", 2500.0, 0.25f, 0xFF42A5F5), // Light Blue
            CategorySpend("Utilities", 1500.0, 0.15f, 0xFF90CAF9), // Lighter Blue
            CategorySpend("Others", 1500.0, 0.15f, 0xFFBBDEFB)    // Pale Blue
        )
    )

    fun getUpcomingBills(): List<UpcomingBill> = listOf(
        UpcomingBill("1", "Netflix", 15.99, "Feb 20", "Entertainment"),
        UpcomingBill("2", "Gym Membership", 50.0, "Feb 22", "Health"),
        UpcomingBill("3", "Water Bill", 30.0, "Feb 25", "Utilities")
    )
    
    fun getNetWorthTrend(timeframe: Timeframe): List<Float> = when (timeframe) {
        Timeframe.WEEKLY -> listOf(15000f, 15200f, 14800f, 15500f, 16000f, 15800f, 16500f)
        Timeframe.MONTHLY -> listOf(12000f, 12500f, 11800f, 13000f, 14500f, 14200f, 15000f, 16500f, 16000f, 17500f)
        Timeframe.YEARLY -> listOf(8000f, 9000f, 8500f, 10000f, 11000f, 10500f, 12000f, 14000f, 13500f, 15000f, 17000f, 16500f)
    }

    fun getBankAccountActivity(bankName: String, timeframe: Timeframe): List<Float> = when (timeframe) {
        Timeframe.WEEKLY -> listOf(200f, 450f, 100f, 800f, 300f, 150f, 600f)
        Timeframe.MONTHLY -> List(30) { (100..1000).random().toFloat() }
        Timeframe.YEARLY -> List(12) { (2000..8000).random().toFloat() }
    }

    fun getCashFlowData(): Pair<List<Float>, List<Float>> {
        // Returns Income vs Expenses for last 6 months
        val income = listOf(35000f, 42000f, 38000f, 45000f, 40000f, 48000f)
        val expenses = listOf(18500f, 22000f, 19000f, 25000f, 21000f, 26000f)
        return Pair(income, expenses)
    }

    fun getPlannerItems(): List<PlannerItem> = listOf(
        PlannerItem("Emergency Fund", 50000.0, 32000.0, "Dec 2026", "Target high priority"),
        PlannerItem("Vacation Plan", 15000.0, 4500.0, "Aug 2026", "Summer trip"),
        PlannerItem("New Laptop", 25000.0, 18000.0, "Oct 2026", "Tech upgrade"),
        PlannerItem("Annual Insurance", 12000.0, 12000.0, "Mar 2026", "Fully funded")
    )
}

data class PlannerItem(
    val name: String,
    val plannedAmount: Double,
    val actualAmount: Double,
    val targetDate: String,
    val note: String
)
