package com.pulseai.ledgerone.presentation.pages

import androidx.compose.animation.AnimatedVisibility
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.pulseai.ledgerone.domain.model.*
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

enum class VerifyMethod {
    ID, SCAN, SCREENSHOT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyPage() {
    val history = remember { MockDataProvider.getVerificationHistory() }
    val scope = rememberCoroutineScope()
    var latestResult by remember { mutableStateOf(MockDataProvider.getLatestVerificationResult()) }
    var showResultModal by remember { mutableStateOf(false) }
    
    var selectedMethod by remember { mutableStateOf<VerifyMethod?>(null) }
    var inputId by remember { mutableStateOf("") }
    var isVerifying by remember { mutableStateOf(false) }

    val bankList = listOf("Telebirr", "CBE Birr", "CBE (Mobile Bank)", "Bank of Abyssinia", "Awash Bank", "Dashen Bank", "Amhara Bank")
    var selectedBank by remember { mutableStateOf(bankList[0]) }
    var expanded by remember { mutableStateOf(false) }

    if (showResultModal) {
        VerificationResultDialog(
            result = latestResult,
            onDismiss = { showResultModal = false }
        )
    }

    CommonPageShell(title = "Verify Transaction") {
        Text(
            text = "Enter transaction details to confirm they are real.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Verify Options
        Text(
            text = "Choose Verification Method",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerifyOptionButton(
                icon = Icons.Outlined.Numbers, 
                label = "Tx ID", 
                isSelected = selectedMethod == VerifyMethod.ID,
                onClick = { selectedMethod = VerifyMethod.ID },
                modifier = Modifier.weight(1f)
            )
            VerifyOptionButton(
                icon = Icons.Outlined.QrCodeScanner, 
                label = "Scan", 
                isSelected = selectedMethod == VerifyMethod.SCAN,
                onClick = { selectedMethod = VerifyMethod.SCAN },
                modifier = Modifier.weight(1f)
            )
            VerifyOptionButton(
                icon = Icons.Outlined.Screenshot, 
                label = "Upload", 
                isSelected = selectedMethod == VerifyMethod.SCREENSHOT,
                onClick = { selectedMethod = VerifyMethod.SCREENSHOT },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Expandable Input Section
        AnimatedVisibility(visible = selectedMethod != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        when (selectedMethod) {
                            VerifyMethod.ID -> {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = { expanded = !expanded },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        OutlinedTextField(
                                            value = selectedBank,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Select Bank / Service") },
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                                            shape = MaterialTheme.shapes.medium,
                                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                                        )

                                        ExposedDropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            bankList.forEach { bank ->
                                                DropdownMenuItem(
                                                    text = { Text(bank) },
                                                    onClick = {
                                                        selectedBank = bank
                                                        expanded = false
                                                    },
                                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                                )
                                            }
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = inputId,
                                    onValueChange = { inputId = it },
                                    label = { Text("Transaction Number") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium,
                                    placeholder = { Text("e.g. TX12345678W") },
                                    singleLine = true
                                )
                            }
                            VerifyMethod.SCAN -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Mock Camera Overlay
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            Icons.Outlined.CenterFocusWeak, 
                                            contentDescription = null, 
                                            tint = Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            "Position QR Code in the frame", 
                                            style = MaterialTheme.typography.labelSmall, 
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                    
                                    // Mock Scanning Line
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.7f)
                                            .height(2.dp)
                                            .background(TelebirrGreen.copy(alpha = 0.5f))
                                    )
                                }
                            }
                            VerifyMethod.SCREENSHOT -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                        .clickable { /* Simulation */ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Outlined.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Select Screenshot", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                            else -> {}
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { 
                                isVerifying = true
                                scope.launch {
                                    kotlinx.coroutines.delay(5000)
                                    isVerifying = false
                                    // Update with a fresh mock result
                                    latestResult = VerificationResult(
                                        amount = (1000..5000).random().toDouble(),
                                        sender = "Abebe Balcha",
                                        receiver = "Fatuma Mohammed",
                                        date = "Today - ${java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}",
                                        isVerified = true
                                    )
                                    showResultModal = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(14.dp),
                            enabled = !isVerifying && (inputId.isNotBlank() || selectedMethod == VerifyMethod.SCAN || selectedMethod == VerifyMethod.SCREENSHOT)
                        ) {
                            if (isVerifying) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Verifying...")
                            } else {
                                Text(if (selectedMethod == VerifyMethod.SCAN) "Scan and Verify" else "Verify Transaction")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Verification Result Card
        Text(
            text = "Latest Result",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        VerificationResultCard(latestResult)

        Spacer(modifier = Modifier.height(32.dp))

        // Verification History
        Text(
            text = "Verification History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        history.forEach { item ->
            VerificationHistoryItemRow(item)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun VerifyOptionButton(
    icon: ImageVector, 
    label: String, 
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = if (isSelected) 4.dp else 0.dp,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label, 
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun VerificationResultCard(result: VerificationResult) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ETB ${String.format("%,.2f", result.amount)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    color = if (result.isVerified) TelebirrGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (result.isVerified) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = if (result.isVerified) TelebirrGreen else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (result.isVerified) "Verified" else "Not Found",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (result.isVerified) TelebirrGreen else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Sender", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(result.sender, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Receiver", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(result.receiver, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Date", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(result.date, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun VerificationHistoryItemRow(item: VerificationHistoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (item.isVerified) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
            contentDescription = null,
            tint = if (item.isVerified) TelebirrGreen else MaterialTheme.colorScheme.error,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "ETB ${String.format("%,.2f", item.amount)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = item.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
            text = if (item.isVerified) "Verified" else "Failed",
            style = MaterialTheme.typography.labelSmall,
            color = if (item.isVerified) TelebirrGreen else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Medium
        )
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

@Composable
fun VerificationResultDialog(result: VerificationResult, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Close")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Verified,
                    contentDescription = null,
                    tint = TelebirrGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Transaction Verified",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "This transaction has been confirmed as authentic in the Telebirr system.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                DetailRow("Amount", "ETB ${String.format("%,.2f", result.amount)}", isHighlight = true)
                DetailRow("Sender", result.sender)
                DetailRow("Receiver", result.receiver)
                DetailRow("Date", result.date)
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ref ID: VER-${(100000..999999).random()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )
}

@Composable
fun DetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value, 
            style = if (isHighlight) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
