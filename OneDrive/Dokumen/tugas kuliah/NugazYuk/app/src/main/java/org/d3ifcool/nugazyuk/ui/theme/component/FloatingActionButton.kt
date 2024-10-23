package org.d3ifcool.nugazyuk.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.d3ifcool.nugazyuk.ui.theme.GreenTertiary

@Composable
fun FloatingActionButtonCustom(
    modifier: Modifier = Modifier,
    actionLogo: ImageVector,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(bottom = 40.dp)
            .size(60.dp)
            .background(
                color = GreenTertiary,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            actionLogo,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp),
        )
    }
}