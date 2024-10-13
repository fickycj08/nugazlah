package top.nabil.nugazlah.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.nabil.nugazlah.R
import top.nabil.nugazlah.ui.component.CustomButton
import top.nabil.nugazlah.ui.component.CustomInput
import top.nabil.nugazlah.ui.theme.JetbrainsMono
import top.nabil.nugazlah.ui.theme.PurpleSurface
import top.nabil.nugazlah.ui.theme.WhitePlain
import top.nabil.nugazlah.ui.theme.WhiteTypo
import top.nabil.nugazlah.util.ValidationError


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    vm: LoginViewModel,
) {
    val state = vm.state.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        vm.eventFlow.collect { event ->
            when (event) {
                is LoginScreenEvent.ShowToast -> {
                    coroutineScope.launch {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(R.drawable.bg_landing),
                contentDescription = null
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .shadow(
                            elevation = 24.dp,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .background(WhitePlain)
                        .clickable {
                            vm.onStateChange(state.copy(isLogin = true))
                        }
                        .padding(vertical = 4.dp, horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.login_text),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 24.dp,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .background(WhitePlain)
                        .clickable {
                            vm.onStateChange(state.copy(isRegister = true))
                        }
                        .padding(vertical = 4.dp, horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.register_text),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.headline_landing_top),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WhiteTypo
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.headline_landing_bottom),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WhiteTypo
                ),
                textAlign = TextAlign.Center
            )
        }
    }

    if (state.isLogin) {
        BasicAlertDialog(
            onDismissRequest = { vm.onStateChange(state.copy(isLogin = false)) },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.login_email_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.login_email_placeholder),
                    text = state.loginEmail,
                    errorMessage = state.loginEmailError,
                    onChange = {
                        vm.onStateChange(state.copy(loginEmail = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.login_password_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    hint = stringResource(id = R.string.login_password_placeholder),
                    text = state.loginPassword,
                    isPasswordShown = state.isPasswordShown,
                    isPasswordTextField = true,
                    onChange = {
                        vm.onStateChange(state.copy(loginPassword = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    onPasswordVisibilityChange = {
                        vm.onStateChange(state.copy(isPasswordShown = !state.isPasswordShown))
                    },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomButton(
                        isLoading = vm.isLoading,
                        text = stringResource(id = R.string.button_text_confirm),
                        onClick = {
                            try {
                                vm.login(context)
                            } catch (e: ValidationError) {
                                return@CustomButton
                            }
                        },
                    )
                }
            }
        }
    }

    if (state.isRegister) {
        BasicAlertDialog(
            onDismissRequest = { vm.onStateChange(state.copy(isRegister = false)) },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.register_fullname_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.register_fullname_placeholder),
                    text = state.registerFullName,
                    onChange = {
                        vm.onStateChange(state.copy(registerFullName = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    testTagSemantic = "InputNameRegister"
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.login_email_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.login_email_placeholder),
                    text = state.registerEmail,
                    onChange = {
                        vm.onStateChange(state.copy(registerEmail = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    testTagSemantic = "InputEmailRegister"
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.login_password_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    hint = stringResource(id = R.string.login_password_placeholder),
                    text = state.registerPassword,
                    isPasswordShown = state.isPasswordShown,
                    isPasswordTextField = true,
                    onChange = {
                        vm.onStateChange(state.copy(registerPassword = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    onPasswordVisibilityChange = {
                        vm.onStateChange(state.copy(isPasswordShown = !state.isPasswordShown))
                    },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomButton(
                        isLoading = vm.isLoading,
                        text = stringResource(id = R.string.button_text_confirm),
                        onClick = {
                            try {
                                vm.register(context = context)
                            } catch (e: ValidationError) {
                                return@CustomButton
                            }
                        },
                    )
                }
            }
        }
    }
}
