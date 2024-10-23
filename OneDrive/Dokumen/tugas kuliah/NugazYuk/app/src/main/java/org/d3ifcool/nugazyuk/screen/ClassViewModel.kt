package org.d3ifcool.nugazyuk.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.d3ifcool.nugazyuk.alarm.AlarmData
import org.d3ifcool.nugazyuk.alarm.Scheduler
import org.d3ifcool.nugazyuk.data.local.Task
import org.d3ifcool.nugazyuk.data.model.deadlineColor
import org.d3ifcool.nugazyuk.data.model.deadlineIcon
import org.d3ifcool.nugazyuk.data.model.determineDeadlineType
import org.d3ifcool.nugazyuk.data.model.taskTypeIcon
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetMyTasksData
import org.d3ifcool.nugazyuk.repository.AuthRepository
import org.d3ifcool.nugazyuk.repository.TaskRepository
import org.d3ifcool.nugazyuk.ui.theme.BlackPlaceholder
import org.d3ifcool.nugazyuk.ui.theme.GreenDone
import org.d3ifcool.nugazyuk.util.ParseTime
import org.d3ifcool.nugazyuk.util.Resource
import kotlin.random.Random

data class TaskData(
    val id: String,
    val title: String,
    val description: String,
    val taskType: String,
    val taskTypeIcon: String,
    val deadline: String,
    val deadlineIcon: String,
    val deadlineColor: Color
)

fun ResponseGetMyTasksData.toTaskData(
    deadlineIcon: String,
    taskTypeIcon: String,
    deadlineColor: Color
): TaskData {
    return TaskData(
        id = this.id,
        title = this.title,
        description = this.description,
        taskType = this.taskType,
        taskTypeIcon = taskTypeIcon,
        deadline = ParseTime.iso8601ToReadable(this.deadline),
        deadlineIcon = deadlineIcon,
        deadlineColor = deadlineColor
    )
}

data class ClassScreenState(
    val classId: String,
    val className: String,
    val isClassMaker: Boolean = false,
    val isTutorialDialogOpen: Boolean = false,
    val tasks: List<TaskData> = emptyList()
)

sealed class ClassScreenEvent {
    data class ShowToast(val message: String) : ClassScreenEvent()
}

class ClassViewModel(
    private val className: String,
    private val classId: String,
    private val classMaker: String,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository,
    private val scheduler: Scheduler
) : ViewModel() {
    private val _state = MutableStateFlow(
        ClassScreenState(
            classId = classId,
            className = className
        )
    )
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ClassScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isGetTasksLoading by mutableStateOf(false)

    init {
        getMyTasks()
        isClassMaker()
    }

    private fun isClassMaker() {
        viewModelScope.launch {
            when (val resource = authRepository.getUserId()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isClassMaker = resource.data == classMaker
                        )
                    }
                }

                is Resource.Error -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMyTasks() {
        val vm = this
        viewModelScope.launch {
            isGetTasksLoading = true
            when (val result = vm.taskRepository.getMyTasks(_state.value.classId)) {
                is Resource.Success -> {
                    // get data from local
                    val registeredTask = taskRepository.getRegisteredTaskAlarm(classId)
                    val unregisteredTask = mutableListOf<Task>()

                    _state.update {
                        it.copy(tasks = result.data
                            ?.sortedByDescending { sortBy ->
                                sortBy.deadline
                            }
                            ?.sortedBy { data ->
                                data.isDone
                            }
                            ?.map { newData ->
                                val deadlineType = determineDeadlineType(newData.deadline)
                                newData.toTaskData(
                                    deadlineIcon = if (newData.isDone) "✅" else deadlineIcon[deadlineType]
                                        ?: "❔",
                                    taskTypeIcon = taskTypeIcon[newData.taskType] ?: "❔",
                                    deadlineColor = if (newData.isDone) GreenDone else deadlineColor[deadlineType]
                                        ?: BlackPlaceholder,
                                )
                            }
                            ?: emptyList())
                    }

                    result.data?.forEach { response ->
                        val isPresent = registeredTask.any {
                            it.taskId == response.id
                        }
                        if (!isPresent && !response.isDone) {
                            unregisteredTask.add(
                                Task(
                                    taskId = response.id,
                                    classId = classId,
                                    isTaskDone = false,
                                    isAlarmRegistered = true,
                                    description = response.description,
                                    title = response.title,
                                    deadline = response.deadline
                                )
                            )
                        }
                    }

                    // register alarm
                    // TODO think about the repetition
                    unregisteredTask.forEachIndexed { index, task ->
                        val alarmId = Random.nextInt()
                        ParseTime.scheduleAlarms(task.deadline).forEach { alarm ->
                            // TEST_VAR
                            if (index != unregisteredTask.lastIndex) {
                                scheduler.schedule(
                                    AlarmData(
                                        id = alarmId,
                                        taskId = task.taskId,
                                        subject = className,
                                        title = task.title,
                                        deadline = ParseTime.iso8601ToReadable(task.deadline),
                                        description = task.description
                                    ),
                                    alarm
                                )
                            } else {
                                scheduler.scheduleActivity(
                                    AlarmData(
                                        id = alarmId,
                                        taskId = task.taskId,
                                        subject = className,
                                        title = task.title,
                                        deadline = ParseTime.iso8601ToReadable(task.deadline),
                                        description = task.description
                                    ),
                                    alarm
                                )
                            }
                        }
                        task.id = alarmId
                    }
                    taskRepository.insertTasksToLocal(unregisteredTask)
                }

                is Resource.Error -> {
                    _eventFlow.emit(ClassScreenEvent.ShowToast(result.message!!))
                }
            }
            isGetTasksLoading = false
        }
    }

    fun onStateChange(state: ClassScreenState) {
        _state.update { state }
    }
}