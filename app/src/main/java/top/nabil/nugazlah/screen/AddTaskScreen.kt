package top.nabil.nugazlah.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import top.nabil.nugazlah.R
import top.nabil.nugazlah.data.model.taskTypeIcon
import top.nabil.nugazlah.ui.component.CustomButton
import top.nabil.nugazlah.ui.component.CustomInput
import top.nabil.nugazlah.ui.component.CustomTextArea
import top.nabil.nugazlah.ui.component.TimePickerDialog
import top.nabil.nugazlah.ui.component.TutorialDialog
import top.nabil.nugazlah.ui.component.TutorialIcon
import top.nabil.nugazlah.ui.theme.GreenChill
import top.nabil.nugazlah.ui.theme.Inter
import top.nabil.nugazlah.ui.theme.PurpleSurface
import top.nabil.nugazlah.ui.theme.PurpleType
import top.nabil.nugazlah.ui.theme.WhitePlaceholder
import top.nabil.nugazlah.ui.theme.WhitePlain
import top.nabil.nugazlah.util.ParseTime
import top.nabil.nugazlah.util.ValidationError

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: AddTaskViewModel
) {
    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState()
    val state = vm.state.collectAsState().value

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        vm.eventFlow.collect { event ->
            when (event) {
                is AddTaskScreenEvent.ShowToast -> {
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
                        text = stringResource(id = R.string.add_task),
                        overflow = TextOverflow.Ellipsis,
                        color = WhitePlain,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        maxLines = 2,
                    )
                }
            )
        },
    ) { paddingValues ->
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
            CustomInput(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.task_title_placeholder),
                text = state.title,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onChange = { vm.onStateChange(state.copy(title = it)) },
                testTagSemantic = "InputTitleTaskTag"
            )

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
            CustomTextArea(
                hint = stringResource(id = R.string.task_description_placeholder),
                text = state.description,
                onChange = { vm.onStateChange(state.copy(description = it)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                testTagSemantic = "InputDescriptionTaskTag"
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
            CustomInput(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.task_detail_placeholder),
                text = state.taskDetail,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onChange = { vm.onStateChange(state.copy(taskDetail = it)) },
                testTagSemantic = "InputDetailTaskTag"
            )

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
            CustomInput(
                modifier = Modifier.fillMaxWidth(),
                hint = stringResource(id = R.string.task_submission_placeholder),
                text = state.taskSubmission,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onChange = { vm.onStateChange(state.copy(taskSubmission = it)) },
                testTagSemantic = "InputSubmissionTaskTag"
            )

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
                    .clickable {
                        vm.onStateChange(state.copy(isTaskTypeDialogOpen = true))
                    }
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
                    vm.onStateChange(state.copy(isTaskTypeDialogOpen = false))
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium)
                    .background(WhitePlain, MaterialTheme.shapes.medium)
                    .clickable {
                        vm.onStateChange(state.copy(isDatePickerDialogOpen = true))
                    }
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .testTag("InputDateTaskTag"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.deadline == "") {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        text = "\uD83D\uDD53",
                        fontSize = 28.sp
                    )
                    Text(
                        text = stringResource(id = R.string.task_deadline_placeholder),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Inter,
                        color = WhitePlaceholder
                    )
                } else {
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

            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomButton(
                    isLoading = vm.isCreateTaskLoading,
                    text = stringResource(id = R.string.button_text_confirm),
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 16.sp,
                        fontFamily = Inter,
                        color = PurpleType
                    ),
                    onClick = {
                        try {
                            vm.createTask()
                        } catch (e: ValidationError) {
                            return@CustomButton
                        }
                    }
                )
            }
        }
    }

    if (state.isTutorialDialogOpen) {
        TutorialDialog {
            vm.onStateChange(state.copy(isTutorialDialogOpen = false))
        }
    }

    if (state.isTaskTypeDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = {
                vm.onStateChange(state.copy(isTaskTypeDialogOpen = false))
            },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (t in taskTypeIcon.keys) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        onClick = {
                            vm.onStateChange(
                                state.copy(
                                    taskType = t,
                                    isTaskTypeDialogOpen = false
                                )
                            )
                        }
                    ) {
                        TutorialIcon(
                            modifier = Modifier.padding(end = 16.dp),
                            icon = taskTypeIcon[t] ?: "",
                        ) {
                            vm.onStateChange(
                                state.copy(
                                    isTutorialDialogOpen = false
                                )
                            )
                        }
                        Text(
                            text = t,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }

    if (state.isDatePickerDialogOpen) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors().copy(
                headlineContentColor = WhitePlain,
                titleContentColor = WhitePlain,
                subheadContentColor = WhitePlain,
                selectedDayContentColor = WhitePlain,
                selectedDayContainerColor = GreenChill
            ),
            tonalElevation = 6.dp,
            onDismissRequest = {
                vm.onStateChange(state.copy(isDatePickerDialogOpen = false))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.onStateChange(
                            state.copy(
                                isDatePickerDialogOpen = false,
                                isTimePickerDialogOpen = true,
                                date = ParseTime.formatDateToString(dateState)
                            )
                        )
                    }
                ) {
                    Text(
                        "OK", color = WhitePlain,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        vm.onStateChange(state.copy(isDatePickerDialogOpen = false))
                    }
                ) {
                    Text(
                        "Cancel", color = WhitePlain,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                    )
                }
            },
        ) {
            DatePicker(
                state = dateState,
            )
        }
    }

    if (state.isTimePickerDialogOpen) {
        TimePickerDialog(
            onDismissRequest = {
                vm.onStateChange(state.copy(isTimePickerDialogOpen = false))
            },
            ignoredConfirmButton = {
                TextButton(
                    onClick = {
                        vm.onStateChange(
                            state.copy(
                                isTimePickerDialogOpen = false,
                                time = ParseTime.formatTimeToString(timeState)
                            )
                        )
                        vm.formatDeadline()
                    }
                ) {
                    Text(
                        "OK",
                        color = WhitePlain,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        vm.onStateChange(state.copy(isTimePickerDialogOpen = false))
                    }
                ) {
                    Text(
                        "Cancel",
                        color = WhitePlain,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                    )
                }
            },
        ) {
            TimePicker(
                state = timeState,
            )
        }
    }
}

