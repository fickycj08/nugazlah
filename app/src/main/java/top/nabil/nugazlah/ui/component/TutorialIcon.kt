package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TutorialIcon(
    modifier: Modifier = Modifier,
    icon: String,
    onDismissRequest: () -> Unit
) {
    Text(
        modifier = modifier.clickable {
            onDismissRequest()
        },
        text = icon,
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 24.sp,
            shadow = Shadow(
                color = Color.Black, offset = Offset(5.0f, 10.0f), blurRadius = 3f
            )
        )
    )
}