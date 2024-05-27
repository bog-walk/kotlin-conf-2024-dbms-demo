package dev.bogwalk.ui.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DBMSColors = darkColorScheme(
    primary = Color(0xffffe81f), // yellow
    secondary = Color(0xffdedede),  // light grey
    background = Color(0xfff8f8ff),  // ghost white
    surface = Color(0xff212121),  // dark grey
    error = Color(0xffe68484),  // red
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color(0xfff8f8ff),  // ghost white
    onError = Color.Black
)

private val DBMSTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 80.sp,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    ),
    headlineLarge = TextStyle(
        fontSize = 40.sp,
        lineHeight = 50.sp,
        textAlign = TextAlign.Center
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontSize = 15.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 15.sp,
        fontFamily = FontFamily.Monospace
    )
)

private val DBMSShapes = Shapes(
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun DBMSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DBMSColors,
        typography = DBMSTypography,
        shapes = DBMSShapes
    ) {
        Surface(content = content)
    }
}