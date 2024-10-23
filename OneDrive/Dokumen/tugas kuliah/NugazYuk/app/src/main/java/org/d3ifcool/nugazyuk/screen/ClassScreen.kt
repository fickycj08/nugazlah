package org.d3ifcool.nugazyuk.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.d3ifcool.nugazyuk.R
import org.d3ifcool.nugazyuk.ui.theme.WhitePlaceholder
import org.d3ifcool.nugazyuk.ui.theme.WhitePlain
import org.d3ifcool.nugazyuk.ui.theme.component.FloatingActionButtonCustom
import org.d3ifcool.nugazyuk.ui.theme.component.TaskCard
import org.d3ifcool.nugazyuk.ui.theme.component.TutorialDialog


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ClassScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: ClassViewModel,
) {
    val state = vm.state.collectAsState().value
    val context = LocalContext.current
    val swipeRefreshState = rememberPullRefreshState(vm.isGetTasksLoading, { vm.getMyTasks() })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(navController.currentBackStackEntry) {
        vm.getMyTasks()
    }

    LaunchedEffect(key1 = true) {
        vm.eventFlow.collect { event ->
            when (event) {
                is ClassScreenEvent.ShowToast -> {
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.className,
                        color = WhitePlain,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        maxLines = 2,
                    )
                }
            )
        },
        floatingActionButton = {
            if (state.isClassMaker) {
                FloatingActionButtonCustom(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .testTag("ActionButtonAddTask"),
                    actionLogo = Icons.Rounded.Add
                ) { navController.navigate("${Screen.AddTaskScreen}/${state.classId}") }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pullRefresh(swipeRefreshState),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                if (!vm.isGetTasksLoading && state.tasks.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.there_is_no_taks),
                            style = MaterialTheme.typography.labelLarge,
                            color = WhitePlaceholder
                        )
                    }
                }

                items(items = state.tasks, key = { it.id }) {
                    TaskCard(
                        deadlineColor = it.deadlineColor,
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        data = it,
                        onClick = {
                            navController.navigate("${Screen.DetailTaskScreen}/${it.id}")
                        },
                        onDismissRequest = {
                            vm.onStateChange(state.copy(isTutorialDialogOpen = true))
                        },
                        testTagSemantic = if (it.title == "Lorem ipsum dolor") "TaskCardTag" else ""
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = vm.isGetTasksLoading,
                state = swipeRefreshState,
                modifier = Modifier
                    .padding(paddingValues)
            )
        }
    }

    if (state.isTutorialDialogOpen) {
        TutorialDialog {
            vm.onStateChange(state.copy(isTutorialDialogOpen = false))
        }
    }
}
