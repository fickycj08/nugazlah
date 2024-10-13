package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.nabil.nugazlah.ui.theme.WhitePlain

@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Row {
        IconButton(
            modifier = Modifier.padding(end = 32.dp),
            onClick = onNavigateBack
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "PengProf (Pengembangan Profesionalisme)",
            color = WhitePlain,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}