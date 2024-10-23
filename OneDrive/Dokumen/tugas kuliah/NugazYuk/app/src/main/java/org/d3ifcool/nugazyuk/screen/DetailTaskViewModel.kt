package org.d3ifcool.nugazyuk.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetDetailTaskData
import org.d3ifcool.nugazyuk.repository.TaskRepository
import org.d3ifcool.nugazyuk.util.ParseTime
import org.d3ifcool.nugazyuk.util.Resource

data class TaskDetailState(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val taskType: String = "",
    val taskDetail: String = "",
    val taskSubmission: String = "",
    val deadline: String = "",
    val isDone: Boolean = false,
    val isTutorialDialogOpen: Boolean = false,
)

fun ResponseGetDetailTaskData.toTaskDetailState(): TaskDetailState {
    return TaskDetailState(
        id = this.id,
        title = this.title,
        description = this.description,
        taskType = this.taskType,
        deadline = ParseTime.iso8601ToReadable(this.deadline),
        taskDetail = this.detail,
        taskSubmission = this.submission,
        isDone = this.status
    )
}

sealed class TaskDetailScreenEvent {
    data class ShowToast(val message: String) : TaskDetailScreenEvent()
}

class TaskDetailViewModel(
    private val taskId: String,
    private val taskRepository: TaskRepository,
    private val navController: NavController
) : ViewModel() {
    private val _state = MutableStateFlow(TaskDetailState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<TaskDetailScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isMarkAsDoneLoading by mutableStateOf(false)
    var isGetDetailLoading by mutableStateOf(false)

    init {
        getTaskDetail()
    }

    fun getTaskDetail() {
        val vm = this
        viewModelScope.launch {
            isGetDetailLoading = true
            when (val result = vm.taskRepository.getTaskDetail(taskId)) {
                is Resource.Success -> {
                    _state.update { result.data?.toTaskDetailState() ?: TaskDetailState() }
                }

                is Resource.Error -> {
                    _eventFlow.emit(TaskDetailScreenEvent.ShowToast(result.message!!))
                }
            }
            isGetDetailLoading = false
        }
    }

    fun markAsDone() {
        // TODO invalidate corresponding alarm
        val vm = this
        viewModelScope.launch {
            if (state.value.isDone) {
                _eventFlow.emit(TaskDetailScreenEvent.ShowToast("This task already done"))
                return@launch
            }
            isMarkAsDoneLoading = true
            when (val result = vm.taskRepository.markTaskDone(taskId)) {
                is Resource.Success -> {
                    _state.update { it.copy(isDone = true) }
                }

                is Resource.Error -> {
                    _eventFlow.emit(TaskDetailScreenEvent.ShowToast(result.message!!))
                }
            }
            isMarkAsDoneLoading = false
            navController.popBackStack()
        }
    }

    fun onStateChange(state: TaskDetailState) {
        _state.update { state }
    }
}