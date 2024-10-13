package top.nabil.nugazlah.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
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
import top.nabil.nugazlah.MainActivity
import top.nabil.nugazlah.data.local.Token
import top.nabil.nugazlah.data.remote.request.LoginRequest
import top.nabil.nugazlah.data.remote.request.RegisterRequest
import top.nabil.nugazlah.repository.AuthRepository
import top.nabil.nugazlah.util.Resource
import top.nabil.nugazlah.util.ValidationError

data class LoginScreenState(
    val isLogin: Boolean = false,
    val isRegister: Boolean = false,
    val loginEmail: String = "",
    val loginEmailError: String = "",
    val loginPassword: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerFullName: String = "",
    val isPasswordShown: Boolean = false,
    val isLoggedIn: Boolean = false
)

sealed class LoginScreenEvent {
    data class ShowToast(val message: String) : LoginScreenEvent()
}

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val navController: NavController
) : ViewModel() {
    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isLoading by mutableStateOf(false)

    init {
        isAlreadyLoggedIn()
    }

    fun onStateChange(state: LoginScreenState) {
        _state.update { state }
    }

    private fun isAlreadyLoggedIn() {
        viewModelScope.launch {
            when (val result = authRepository.isLoggedIn()) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoggedIn = result.data!!) }
                }

                is Resource.Error -> {
                    _eventFlow.emit(LoginScreenEvent.ShowToast(result.message!!))
                }
            }
        }
    }

    fun login(context: Context) {
        val value = _state.value
        if (value.loginEmail.isEmpty()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Email tidak boleh kosong"))
            }
            throw ValidationError("Email tidak boleh kosong")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(value.loginEmail).matches()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Email harus berformat email"))
            }
            throw ValidationError("Email harus berformat email")
        }
        if (value.loginPassword.length < 8) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Password minimal 8 huruf"))
            }
            throw ValidationError("Password minimal 8 huruf")
        }

        viewModelScope.launch {
            isLoading()
            val response =
                authRepository.login(LoginRequest(value.loginEmail, value.loginPassword))
            val responseData = response.data
            when (response) {
                is Resource.Success -> {
                    authRepository.saveToken(
                        Token(
                            token = responseData?.data?.accessToken ?: "",
                            userId = responseData?.data?.userId ?: ""
                        )
                    )
                    _eventFlow.emit(LoginScreenEvent.ShowToast("Login success"))

                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                    // TEST_VAR
                    Runtime.getRuntime().exit(0)
                }

                is Resource.Error -> {
                    _eventFlow.emit(LoginScreenEvent.ShowToast(response.message!!))
                }
            }
            isNotLoading()
        }
    }

    fun register(context: Context) {
        val value = _state.value
        if (value.registerFullName.isEmpty()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Nama tidak boleh kosong"))
            }
            throw ValidationError("Nama tidak boleh kosong")
        }
        if (value.registerEmail.isEmpty()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Email tidak boleh kosong"))
            }
            throw ValidationError("Email tidak boleh kosong")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(value.registerEmail).matches()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Email harus berformat email"))
            }
            throw ValidationError("Email harus berformat email")
        }
        if (value.registerPassword.length < 8) {
            viewModelScope.launch {
                _eventFlow.emit(LoginScreenEvent.ShowToast("Password minimal 8 huruf"))
            }
            throw ValidationError("Password minimal 8 huruf")
        }

        viewModelScope.launch {
            isLoading()
            val response =
                authRepository.register(
                    RegisterRequest(
                        value.registerEmail,
                        value.registerFullName,
                        value.registerPassword
                    )
                )

            // Tambahkan log untuk memeriksa respons dari server
            Log.d("RegisterResponse", "Response: $response")

            val responseData = response.data
            when (response) {
                is Resource.Success -> {
                    Log.d("RegisterResponse", "Registration successful: ${response.data}")

                    authRepository.saveToken(
                        Token(
                            token = responseData?.data?.accessToken ?: "",
                            userId = responseData?.data?.userId ?: ""
                        )
                    )
                    _eventFlow.emit(LoginScreenEvent.ShowToast("Register success"))

                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)

                    // TEST_VAR
                    Runtime.getRuntime().exit(0)
                }

                is Resource.Error -> {
                    Log.e("RegisterResponse", "Registration error: ${response.message}")
                    _eventFlow.emit(LoginScreenEvent.ShowToast(response.message!!))
                }
            }
            isNotLoading()
        }
    }

    private fun isLoading() {
        isLoading = true
    }

    private fun isNotLoading() {
        isLoading = false
    }
}