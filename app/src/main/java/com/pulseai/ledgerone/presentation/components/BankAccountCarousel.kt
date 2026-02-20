package com.pulseai.ledgerone.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material3.*
import androidx.compose.material3.carousel.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pulseai.ledgerone.presentation.theme.*
import com.pulseai.ledgerone.presentation.mock.MockDataProvider
import com.pulseai.ledgerone.presentation.mock.Timeframe

data class BankAccount(
    val name: String,
    val balance: Double,
    val color: Color,
    val imageUrl: String = "https://images.unsplash.com/photo-1550565118-3a14e8d0386f?q=80&w=200&auto=format&fit=crop"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankAccountCarousel(
    modifier: Modifier = Modifier
) {
    val accounts = listOf(
        BankAccount("Telebirr", 12450.00, TelebirrGreen),
        BankAccount("CBE", 45200.00, CBEPurple),
        BankAccount("Abyssinia", 8900.00, AbyssiniaRed)
    )

    val carouselState = rememberCarouselState { accounts.size }

    HorizontalMultiBrowseCarousel(
        state = carouselState,
        preferredItemWidth = 280.dp,
        itemSpacing = 16.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { index ->
        val account = accounts[index]
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = 4.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = MaterialTheme.shapes.extraLarge,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = account.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = account.color,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "ETB ${String.format("%,.2f", account.balance)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalance,
                            contentDescription = null,
                            tint = account.color.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Activity",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                ActivityChartWithFilters(
                    initialData = MockDataProvider.getBankAccountActivity(account.name, Timeframe.WEEKLY),
                    onTimeframeChange = { timeframe -> 
                        MockDataProvider.getBankAccountActivity(account.name, timeframe) 
                    },
                    lineColor = account.color,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
