package org.d3ifcool.nugazyuk.screen

import android.content.Context
import android.content.Intent
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
import org.d3ifcool.nugazyuk.MainActivity
import org.d3ifcool.nugazyuk.data.remote.request.RequestCreateClass
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetMyClassesData
import org.d3ifcool.nugazyuk.repository.AuthRepository
import org.d3ifcool.nugazyuk.repository.ClassRepository
import org.d3ifcool.nugazyuk.util.Resource
import org.d3ifcool.nugazyuk.util.ValidationError

data class ClassData(
    val id: String,
    val name: String,
    val description: String,
    val lecturer: String,
    val icon: String,
    val code: String,
    val maker: String
)

fun ResponseGetMyClassesData.toClassData(): ClassData {
    return ClassData(
        id = this.id,
        name = this.name,
        description = this.description,
        lecturer = this.lecturer,
        icon = this.icon,
        code = this.code,
        maker = this.maker
    )
}

data class HomeScreenState(
    val classCode: String = "",
    val classLecturer: String = "",
    val className: String = "",
    val classDescription: String = "",
    val classIcon: String = "",
    val isCreateClassLoading: Boolean = false,
    val isDialogOpen: Boolean = false,
    val isDialogJoinRoomOpen: Boolean = false,
    val isDialogMakeRoomOpen: Boolean = false,
    val isDialogLogoutOpen: Boolean = false,
    val classes: List<ClassData> = emptyList()
)

sealed class HomeScreenEvent {
    data class ShowToast(val message: String) : HomeScreenEvent()
}

class HomeViewModel(
    private val classRepository: ClassRepository,
    private val authRepository: AuthRepository,
    private val navController: NavController
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<HomeScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isGetClassesLoading by mutableStateOf(false)

    init {
        getMyClasses()
    }

    fun getMyClasses() {
        val vm = this
        viewModelScope.launch {
            isGetClassesLoading = true
            when (val result = vm.classRepository.getMyClasses()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(classes = result.data?.map { newData -> newData.toClassData() }
                            ?: emptyList())
                    }
                }

                is Resource.Error -> {
                    _eventFlow.emit(HomeScreenEvent.ShowToast(result.message!!))
                }
            }
            isGetClassesLoading = false
        }
    }

    fun createMyClass() {
        val vm = this
        val value = _state.value
        val userMaker = "NamaPembuatKelas"  // Misalnya, ambil dari session login

        viewModelScope.launch {
            isGetClassesLoading = true
            when (val result = vm.classRepository.createClass(
                RequestCreateClass(
                    description = value.classDescription,
                    icon = value.classIcon,
                    lecturer = value.classLecturer,
                    name = value.className,
                    maker = userMaker  // Pastikan maker dikirim
                )
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            classDescription = "",
                            classIcon = "",
                            classLecturer = "",
                            className = "",
                            isDialogMakeRoomOpen = false,
                            isDialogOpen = false
                        )
                    }
                    getMyClasses()
                }

                is Resource.Error -> {
                    _eventFlow.emit(HomeScreenEvent.ShowToast(result.message!!))
                }
            }
            isGetClassesLoading = false
        }
    }


    fun joinClass() {
        val vm = this
        val value = _state.value
        if (value.classCode.length < 6) {
            viewModelScope.launch {
                _eventFlow.emit(HomeScreenEvent.ShowToast("Kode kelas minimal 6 huruf"))
            }
            throw ValidationError("Kode kelas minimal 6 huruf")
        }

        viewModelScope.launch {
            isGetClassesLoading = true
            when (val result = vm.classRepository.joinClass(value.classCode)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            classCode = "",
                            isDialogJoinRoomOpen = false,
                            isDialogOpen = false
                        )
                    }
                    getMyClasses()
                }

                is Resource.Error -> {
                    _eventFlow.emit(HomeScreenEvent.ShowToast(result.message!!))
                }
            }
            isGetClassesLoading = false
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isDialogLogoutOpen = false) }
            authRepository.logout()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            // TEST_VAR
            Runtime.getRuntime().exit(0)
        }
    }

    fun onStateChange(state: HomeScreenState) {
        _state.update { state }
    }
}