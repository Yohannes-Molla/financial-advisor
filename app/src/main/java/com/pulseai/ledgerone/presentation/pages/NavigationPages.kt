package com.pulseai.ledgerone.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pulseai.ledgerone.presentation.dashboard.DashboardScreen
import com.pulseai.ledgerone.presentation.mock.MockDataProvider
import com.pulseai.ledgerone.presentation.mock.PlannerItem
import com.pulseai.ledgerone.presentation.theme.*

@Composable
fun DashboardPage() {
    DashboardScreen()
}

@Composable
fun InsightsPage() {
    AnalyticsPage()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerPage() {
    val items = remember { MockDataProvider.getPlannerItems() }
    var selectedGoal by remember { mutableStateOf<PlannerItem?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showDetail by remember { mutableStateOf(false) }
    
    CommonPageShell(title = "Financial Planner") {
        Text(
            text = "Track your savings goals and planned expenses.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        items.forEach { item ->
            PlannerItemCard(
                item = item,
                onClick = {
                    selectedGoal = item
                    showDetail = true
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(Icons.Outlined.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add New Goal")
        }
    }

    if (showDetail && selectedGoal != null) {
        ModalBottomSheet(
            onDismissRequest = { showDetail = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            GoalDetailBottomSheetContent(
                goal = selectedGoal!!,
                onClose = { showDetail = false }
            )
        }
    }
}

@Composable
fun PlannerItemCard(item: PlannerItem, onClick: () -> Unit) {
    val progress = (item.actualAmount / item.plannedAmount).toFloat().coerceIn(0f, 1f)
    val remainder = item.plannedAmount - item.actualAmount
    
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (remainder <= 0) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = TelebirrGreen, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        text = "ETB ${String.format("%,.0f", remainder)} left",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (progress >= 1f) TelebirrGreen else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ETB ${String.format("%,.0f", item.actualAmount)} / ${String.format("%,.0f", item.plannedAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = item.targetDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun GoalDetailBottomSheetContent(goal: PlannerItem, onClose: () -> Unit) {
    var budgetAmount by remember { mutableStateOf(goal.plannedAmount.toString()) }
    var targetDate by remember { mutableStateOf(goal.targetDate) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = goal.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Outlined.Close, contentDescription = "Close")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Goal Details",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = budgetAmount,
            onValueChange = { budgetAmount = it },
            label = { Text("Budget Amount (ETB)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            leadingIcon = { Icon(Icons.Outlined.AccountBalanceWallet, contentDescription = null) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = targetDate,
            onValueChange = { targetDate = it },
            label = { Text("Target Date (Timer)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            leadingIcon = { Icon(Icons.Outlined.Timer, contentDescription = null) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Status: ${if (goal.actualAmount >= goal.plannedAmount) "Completed" else "In Progress"}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (goal.actualAmount >= goal.plannedAmount) TelebirrGreen else MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Actual Savings: ETB ${String.format("%,.0f", goal.actualAmount)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { onClose() },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("Save Changes")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(
            onClick = { onClose() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ProfilePage() {
    CommonPageShell(title = "User Profile") {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("YM", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Yohannes Molla", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("yohannes.molla@pulseai.com", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            ProfileSettingItem(Icons.Outlined.Edit, "Edit Profile")
            ProfileSettingItem(Icons.Outlined.Notifications, "Notification Settings")
            ProfileSettingItem(Icons.Outlined.Language, "Language", "English")
            ProfileSettingItem(Icons.Outlined.Logout, "Log Out", textColor = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun SettingsPage() {
    CommonPageShell(title = "App Settings") {
        Column {
            SettingsSection("General") {
                ProfileSettingItem(Icons.Outlined.Palette, "Theme", "Light")
                ProfileSettingItem(Icons.Outlined.Visibility, "Privacy Mode", "Enabled")
            }
            SettingsSection("Preferences") {
                ProfileSettingItem(Icons.Outlined.AutoGraph, "Sync Frequency", "Real-time")
                ProfileSettingItem(Icons.Outlined.CloudUpload, "Data Backup")
            }
            SettingsSection("Support") {
                ProfileSettingItem(Icons.Outlined.HelpOutline, "Help Center")
                ProfileSettingItem(Icons.Outlined.Info, "About LedgerOne")
            }
        }
    }
}

@Composable
fun SecurityPage() {
    CommonPageShell(title = "Security & Privacy") {
        Column {
            SecurityItem(Icons.Outlined.Fingerprint, "Biometric Authentication", "Use fingerprint to unlock", true)
            SecurityItem(Icons.Outlined.Lock, "Two-Factor Authentication", "Enable SMS verification", false)
            SecurityItem(Icons.Outlined.Key, "Change PIN", "Last changed 3 months ago")
            SecurityItem(Icons.Outlined.Devices, "Active Sessions", "3 devices connected")
        }
    }
}

@Composable
fun LinkedBanksPage() {
    CommonPageShell(title = "Linked Accounts") {
        Column {
            BankItem("Telebirr", "ETB 12,450.00", TelebirrGreen)
            BankItem("CBE", "ETB 45,200.00", CBEPurple)
            BankItem("Abyssinia", "ETB 8,900.00", AbyssiniaRed)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Link New Bank Account")
            }
        }
    }
}

@Composable
fun BudgetPage() {
    CommonPageShell(title = "Budgeting") {
        Text("Track your monthly budgets and limits.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun CommonPageShell(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        content()
    }
}

@Composable
fun ProfileSettingItem(icon: ImageVector, label: String, value: String? = null, textColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 16.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = textColor)
        Spacer(modifier = Modifier.weight(1f))
        if (value != null) {
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SecurityItem(icon: ImageVector, title: String, subtitle: String, isSwitch: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (isSwitch) {
            Switch(checked = true, onCheckedChange = { })
        } else {
            Icon(Icons.Outlined.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun BankItem(name: String, balance: String, brandColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(brandColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.AccountBalance, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(text = "Active account", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = balance, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}
