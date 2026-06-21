//package com.example.utilityapp.ui.theme
//
//import androidx.compose.material3.Typography
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//
//// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//    /* Other default text styles to override
//    titleLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )
//    */
//)

package com.example.utilityapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.utilityapp.R

val SegoeUI = FontFamily(
    Font(R.font.segoeui,       FontWeight.Normal),
    Font(R.font.segoeui_bold,  FontWeight.Bold),
    Font(R.font.segoeui_light, FontWeight.Light)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = SegoeUI,
        fontWeight = FontWeight.Light,
        fontSize   = 48.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = SegoeUI,
        fontWeight = FontWeight.Light,
        fontSize   = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SegoeUI,
        fontWeight = FontWeight.Light,
        fontSize   = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = SegoeUI,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SegoeUI,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp
    )
)