package com.kakao.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kakao.presentation.R

val fontFamily = FontFamily(
  Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold),
  Font(R.font.pretendard_bold, FontWeight.Bold),
  Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
  Font(R.font.pretendard_regular, FontWeight.Medium)
)

val typography = Typography(
  displayLarge = TextStyle(
    fontSize = 57.sp,
    lineHeight = 64.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.ExtraBold
  ),
  displayMedium = TextStyle(
    fontSize = 45.sp,
    lineHeight = 52.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.ExtraBold
  ),
  displaySmall = TextStyle(
    fontSize = 36.sp,
    lineHeight = 44.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.ExtraBold
  ),
  headlineLarge = TextStyle(
    fontSize = 32.sp,
    lineHeight = 40.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold
  ),
  headlineMedium = TextStyle(
    fontSize = 28.sp,
    lineHeight = 36.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold
  ),
  headlineSmall = TextStyle(
    fontSize = 24.sp,
    lineHeight = 32.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.SemiBold
  ),
  titleLarge = TextStyle(
    fontSize = 22.sp,
    lineHeight = 28.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold
  ),
  titleMedium = TextStyle(
    fontSize = 16.sp,
    lineHeight = 24.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.15.sp
  ),
  titleSmall = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.1.sp
  ),
  bodyLarge = TextStyle(
    fontSize = 16.sp,
    lineHeight = 24.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.15.sp
  ),
  bodyMedium = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.25.sp
  ),
  bodySmall = TextStyle(
    fontSize = 12.sp,
    lineHeight = 16.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.4.sp
  ),
  labelLarge = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.1.sp
  ),
  labelMedium = TextStyle(
    fontSize = 12.sp,
    lineHeight = 16.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.5.sp
  ),
  labelSmall = TextStyle(
    fontSize = 11.sp,
    lineHeight = 16.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.5.sp
  )
)