package org.d3ifcool.nugazyuk.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.d3ifcool.nugazyuk.R

val JetbrainsMono = FontFamily(
    Font(R.font.jetbrainsmono_regular, FontWeight.Normal),
    Font(R.font.jetbrainsmono_medium, FontWeight.Medium),
    Font(R.font.jetbrainsmono_bold, FontWeight.Bold),
)

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_semibold, FontWeight.SemiBold)
)

private val JotiOne = FontFamily(
    Font(R.font.jotione_regular, FontWeight.Normal),
)


// Display, Headline, Title, Body, Label

// Set of Material typography styles to start with
val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = JetbrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    headlineSmall = TextStyle(
        fontFamily = JetbrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    titleLarge = TextStyle(
        fontFamily = JetbrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
        color = DarkTitle
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = WhiteTypo
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        color = BlackTypo
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = BlackTypo
    ),
    labelLarge = TextStyle(
        fontFamily = JetbrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = DarkTitle
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = JetbrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 8.sp,
        lineHeight = 8.sp,
        letterSpacing = 0.5.sp,
        color = BlackPlaceholder
    ),
)