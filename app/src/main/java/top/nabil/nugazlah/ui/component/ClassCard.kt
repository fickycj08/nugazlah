package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.nabil.nugazlah.data.model.ClassCardState
import top.nabil.nugazlah.screen.ClassData
import top.nabil.nugazlah.ui.theme.GreenCard
import top.nabil.nugazlah.ui.theme.JetbrainsMono
import top.nabil.nugazlah.ui.theme.OrangeGuru
import top.nabil.nugazlah.ui.theme.PurpleSurface

@Composable
fun ClassCard(
    modifier: Modifier = Modifier,
    data: ClassData,
    onClick: (String) -> Unit = {},
    testTagSemantic: String = ""
) {
    Column(
        modifier = modifier
            .shadow(
                10.dp,
                MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .background(GreenCard)
            .clickable {
                onClick(data.id)
            }
            .padding(8.dp)
            .testTag(testTagSemantic),
        horizontalAlignment = Alignment.End
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 12.sp,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = data.icon,
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 24.sp,
                    shadow = Shadow(
                        color = Color.Black, offset = Offset(5.0f, 10.0f), blurRadius = 3f
                    )
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = data.code,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = JetbrainsMono,
                color = PurpleSurface
            )
            Text(
                text = data.lecturer,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = JetbrainsMono,
                color = OrangeGuru
            )
        }
    }
}