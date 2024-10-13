package top.nabil.nugazlah.screen

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
import top.nabil.nugazlah.data.model.deadlineColor
import top.nabil.nugazlah.data.model.deadlineIcon
import top.nabil.nugazlah.data.model.determineDeadlineType
import top.nabil.nugazlah.data.model.taskTypeIcon
import top.nabil.nugazlah.data.remote.request.RequestCreateTask
import top.nabil.nugazlah.repository.TaskRepository
import top.nabil.nugazlah.ui.theme.BlackPlaceholder
import top.nabil.nugazlah.util.ParseTime
import top.nabil.nugazlah.util.Resource
import top.nabil.nugazlah.util.ValidationError

data class AddTaskScreenState(
    val title: String = "",
    val description: String = "",
    val taskType: String = "Proposal",
    val taskDetail: String = "",
    val taskSubmission: String = "",
    val date: String = "",
    val time: String = "",
    val deadline: String = "",
    val _deadline: String = "",
    val isTaskTypeDialogOpen: Boolean = false,
    val isTutorialDialogOpen: Boolean = false,
    val isDatePickerDialogOpen: Boolean = false,
    val isTimePickerDialogOpen: Boolean = false,
)

fun AddTaskScreenState.toRequestCreateTask(classId: String): RequestCreateTask {
    return RequestCreateTask(
        classId = classId,
        deadline = this._deadline,
        description = this.description,
        title = this.title,
        taskType = this.taskType,
        detail = this.taskDetail,
        submission = this.taskSubmission,
    )
}

sealed class AddTaskScreenEvent {
    data class ShowToast(val message: String) : AddTaskScreenEvent()
}

class AddTaskViewModel(
    private val classId: String,
    private val taskRepository: TaskRepository,
    private val navController: NavController
) : ViewModel() {
    private val _state = MutableStateFlow(AddTaskScreenState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AddTaskScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isCreateTaskLoading by mutableStateOf(false)

    fun formatDeadline() {
        _state.update {
            it.copy(
                _deadline = ParseTime.dateTimeStringToIso8601(_state.value.date, _state.value.time),
                deadline = ParseTime.iso8601ToReadable(
                    ParseTime.dateTimeStringToIso8601(
                        _state.value.date,
                        _state.value.time
                    )
                )
            )
        }
    }

    fun createTask() {
        val vm = this
        val value = _state.value
        if (value.title.length < 3) {
            viewModelScope.launch {
                _eventFlow.emit(AddTaskScreenEvent.ShowToast("Judul tugas minimal 3 huruf"))
            }
            throw ValidationError("Judul tugas minimal 3 huruf")
        }
        if (value.taskDetail.length < 3) {
            viewModelScope.launch {
                _eventFlow.emit(AddTaskScreenEvent.ShowToast("Detail tugas minimal 3 huruf"))
            }
            throw ValidationError("Detail tugas minimal 3 huruf")
        }
        if (value.description.length < 10) {
            viewModelScope.launch {
                _eventFlow.emit(AddTaskScreenEvent.ShowToast("Deskripsi tugas minimal 10 huruf"))
            }
            throw ValidationError("Deskripsi tugas minimal 10 huruf")
        }
        if (value.taskSubmission.length < 3) {
            viewModelScope.launch {
                _eventFlow.emit(AddTaskScreenEvent.ShowToast("Pengumpulan tugas minimal 3 huruf"))
            }
            throw ValidationError("Pengumpulan tugas minimal 3 huruf")
        }
        if (value.deadline.isEmpty()) {
            viewModelScope.launch {
                _eventFlow.emit(AddTaskScreenEvent.ShowToast("Deadline tugas tidak boleh kosong"))
            }
            throw ValidationError("Deadline tugas tidak boleh kosong")
        }

        viewModelScope.launch {
            isCreateTaskLoading = true
            when (val result =
                vm.taskRepository.createTask(_state.value.toRequestCreateTask(classId))) {
                is Resource.Success -> {
                    navController.popBackStack()
                }

                is Resource.Error -> {
                    _eventFlow.emit(AddTaskScreenEvent.ShowToast(result.message!!))
                }
            }
            isCreateTaskLoading = false
        }
    }

    fun onStateChange(state: AddTaskScreenState) {
        _state.update { state }
    }
}
