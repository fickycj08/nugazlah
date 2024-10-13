package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import top.nabil.nugazlah.data.model.TutorialData
import top.nabil.nugazlah.ui.theme.PurpleSecondSurface
import top.nabil.nugazlah.ui.theme.PurpleSurface
import top.nabil.nugazlah.ui.theme.WhitePlain

@Composable
fun TutorialDialog(
    onDismissRequest: () -> Unit
) {
    val deadlineIcon = listOf(
        TutorialData(
            icon = "✅",
            description = "Dah kelar bang. Mantap"
        ),
        TutorialData(
            icon = "\uD83D\uDE0E",
            description = "Masih lebih dari seminggu. Sans aja"
        ),
        TutorialData(
            icon = "\uD83D\uDE36\u200D\uD83C\uDF2B\uFE0F",
            description = "Tinggal 1-7 hari bang. Cepetan dikit"
        ),
        TutorialData(
            icon = "⛔",
            description = "Kurang dari 1 hari bang. Buruan"
        ),
        TutorialData(
            icon = "☠\uFE0F",
            description = "Dah lewat deadline bang. Ckck"
        )
    )

    val taskIcon = listOf(
        TutorialData(
            icon = "\uD83D\uDCA1",
            description = "Quiz bang. Kek isian abc gitu"
        ),
        TutorialData(
            icon = "\uD83D\uDCDD",
            description = "Essay bang. Awokawokawok"
        ),
        TutorialData(
            icon = "\uD83D\uDDF3\uFE0F",
            description = "Response bang. Kek pendapat gitu"
        ),
        TutorialData(
            icon = "\uD83D\uDCD1",
            description = "Proposal bang. U know la"
        ),
        TutorialData(
            icon = "\uD83E\uDDF0",
            description = "Project bang. Si paling project gatuh"
        )
    )


    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(PurpleSurface, MaterialTheme.shapes.medium)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TutorialCard(title = "Icon Deadline", tutorials = deadlineIcon)
            Spacer(modifier = Modifier.height(8.dp))
            TutorialCard(title = "Icon Tugas", tutorials = taskIcon)
        }
    }
}

@Composable
fun TutorialCard(
    modifier: Modifier = Modifier,
    title: String,
    tutorials: List<TutorialData>
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(PurpleSecondSurface, MaterialTheme.shapes.medium)
            .padding(24.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.labelLarge,
            text = title,
            color = WhitePlain
        )
        for (t in tutorials) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = t.icon, fontSize = 24.sp
                )
                Text(
                    text = t.description,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    color = WhitePlain,
                )
            }
        }
    }
}














