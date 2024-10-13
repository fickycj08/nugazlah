package top.nabil.nugazlah.screen

import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import top.nabil.nugazlah.R
import top.nabil.nugazlah.data.model.TaskData
import top.nabil.nugazlah.data.model.taskTypeIcon
import top.nabil.nugazlah.ui.component.TaskCard
import top.nabil.nugazlah.ui.component.TutorialDialog
import top.nabil.nugazlah.ui.component.TutorialIcon
import top.nabil.nugazlah.ui.theme.BlackTypo
import top.nabil.nugazlah.ui.theme.DarkSurface
import top.nabil.nugazlah.ui.theme.GreenCard
import top.nabil.nugazlah.ui.theme.WhitePlaceholder
import top.nabil.nugazlah.ui.theme.WhitePlain

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DetailTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: TaskDetailViewModel
) {
    val state = vm.state.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val swipeRefreshState = rememberPullRefreshState(vm.isGetDetailLoading, { vm.getTaskDetail() })

    LaunchedEffect(key1 = true) {
        vm.eventFlow.collect { event ->
            when (event) {
                is TaskDetailScreenEvent.ShowToast -> {
                    coroutineScope.launch {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TextButton(
                        modifier = Modifier.padding(top = 4.dp),
                        onClick = vm::markAsDone
                    ) {
                        Text(
                            text = if (state.isDone) "✅" else "☑\uFE0F",
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                shadow = Shadow(
                                    color = BlackTypo,
                                    offset = Offset(4.0f, 8.0f),
                                    blurRadius = 3f
                                )
                            )
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.task_detail),
                        color = WhitePlain,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        maxLines = 2,
                    )
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pullRefresh(swipeRefreshState),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.task_title),
                    style = MaterialTheme.typography.labelMedium
                )
                TextBox(text = state.title, textStyle = MaterialTheme.typography.titleLarge)

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.task_description),
                    style = MaterialTheme.typography.labelMedium
                )
                TextBox(
                    text = state.description,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 18.sp,
                        letterSpacing = 0.1.sp
                    )
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.task_source),
                    style = MaterialTheme.typography.labelMedium
                )
                if (URLUtil.isValidUrl(state.taskDetail)) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(GreenCard, MaterialTheme.shapes.small)
                            .clickable {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(state.taskDetail)
                                )
                                ContextCompat.startActivity(context, browserIntent, null)
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.task_source_wording),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    TextBox(
                        text = state.taskDetail,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            letterSpacing = 0.1.sp
                        )
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.task_submission),
                    style = MaterialTheme.typography.labelMedium
                )
                if (URLUtil.isValidUrl(state.taskDetail)) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(GreenCard, MaterialTheme.shapes.small)
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .clickable {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(state.taskSubmission)
                                )
                                ContextCompat.startActivity(context, browserIntent, null)
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.task_submission_wording),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    TextBox(
                        text = state.taskSubmission,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            letterSpacing = 0.1.sp
                        )
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.task_type),
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(WhitePlain)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TutorialIcon(
                        modifier = Modifier.padding(end = 16.dp),
                        icon = taskTypeIcon[state.taskType] ?: ""
                    ) {
                        vm.onStateChange(state.copy(isTutorialDialogOpen = true))
                    }
                    Text(
                        text = state.taskType,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.task_deadline),
                    style = MaterialTheme.typography.labelMedium
                )
                TextBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = state.deadline,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                )
            }

            if (vm.isGetDetailLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkSurface)
                )
            }

            PullRefreshIndicator(
                refreshing = vm.isGetDetailLoading || vm.isMarkAsDoneLoading,
                state = swipeRefreshState,
                modifier = Modifier
                    .padding(paddingValues)
            )
        }
    }

    if (state.isTutorialDialogOpen) {
        TutorialDialog {
            vm.onStateChange(state.copy(isTutorialDialogOpen = true))
        }
    }
}

@Composable
fun TextBox(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(WhitePlain)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text, style = textStyle
        )
    }
}
