package com.pulseai.ledgerone.presentation.theme

import androidx.compose.ui.graphics.Color

// Azure Palette (Lightish Blue)
val AzurePrimary = Color(0xFF0056D2)
val AzurePrimaryVariant = Color(0xFF003C96)
val AzureBackgroundLight = Color(0xFFF1F6FF)
val AzureSurfaceLight = Color(0xFFFFFFFF)
val AzureOnSurfaceLight = Color(0xFF1A1C1E)
val AzureSecondaryLight = Color(0xFFE3F2FD)

val AzureBackgroundDark = Color(0xFF0B141E)
val AzureSurfaceDark = Color(0xFF15202B)
val AzureOnSurfaceDark = Color(0xFFE1E2E5)

// Burgundy Palette (Psychology of Money: Ambition, Power)
val BurgundyRed = Color(0xFF800020)
val RichMerlot = Color(0xFF5D0016)
val BurgundyBackgroundLight = Color(0xFFFFFBFA) // Warm white
val BurgundySurfaceLight = Color(0xFFFFFFFF)
val BurgundySecondaryLight = Color(0xFFF8EBD7) // Champagne/Gold tint for secondary

val BurgundyBackgroundDark = Color(0xFF120205) // Deepest Merlot/Black
val BurgundySurfaceDark = Color(0xFF200509)
val BurgundyPrimaryDark = Color(0xFFFFB3B3) // Lighter red for dark mode contrast
val BurgundyOnPrimaryDark = Color(0xFF4A000E)

// Bank Brand Colors
// CBE (Commercial Bank of Ethiopia): Purple & Gold
val CBEPurple = Color(0xFF673AB7)
val CBEGold = Color(0xFFFFC107)

// Bank of Abyssinia: Red & Gold/Cream
val AbyssiniaRed = Color(0xFFD32F2F)
val AbyssiniaCream = Color(0xFFFFFDE7)

// Telebirr: Green & Blue
val TelebirrGreen = Color(0xFF01D167)
val TelebirrBlue = Color(0xFF2196F3)

// Awash: Blue & Orange (Approximate)
val AwashBlue = Color(0xFF0D47A1)
val AwashOrange = Color(0xFFFF6D00)

// Standard Neutrals (for compatibility)
val Neutral99 = Color(0xFFFCFCFC)
val Neutral95 = Color(0xFFF1F3F5)
val Neutral20 = Color(0xFF343A40)
val Neutral10 = Color(0xFF212529)

// Theme Mappings
val BackgroundLight = BurgundyBackgroundLight
val SurfaceLight = BurgundySurfaceLight
val OnSurfaceLight = Neutral10
val PrimaryLight = BurgundyRed
val OnPrimaryLight = Color.White
val SecondaryLight = BurgundySecondaryLight

val BackgroundDark = BurgundyBackgroundDark
val SurfaceDark = BurgundySurfaceDark
val OnSurfaceDark = Color(0xFFE6E1E5)
val PrimaryDark = BurgundyPrimaryDark
val OnPrimaryDark = BurgundyOnPrimaryDark
