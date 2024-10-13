package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import top.nabil.nugazlah.ui.theme.GreenCard
import top.nabil.nugazlah.ui.theme.Inter
import top.nabil.nugazlah.ui.theme.PurpleType
import top.nabil.nugazlah.ui.theme.WhitePlaceholder
import top.nabil.nugazlah.ui.theme.WhitePlain

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = GreenCard,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(
        fontFamily = Inter,
        color = PurpleType
    ),
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .clickable {
                onClick()
            }
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = textStyle.color,
                modifier = Modifier
                    .size(with(LocalDensity.current) { textStyle.toSpanStyle().fontSize.toDp() })
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = textStyle
        )
    }
}