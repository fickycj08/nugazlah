package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import top.nabil.nugazlah.R
import top.nabil.nugazlah.ui.theme.BlackTypo
import top.nabil.nugazlah.ui.theme.Inter
import top.nabil.nugazlah.ui.theme.RedDanger
import top.nabil.nugazlah.ui.theme.WhitePlaceholder
import top.nabil.nugazlah.ui.theme.WhitePlain

@Composable
fun CustomInput(
    modifier: Modifier = Modifier,
    hint: String = "",
    isEnable: Boolean = true,
    text: String = "",
    errorMessage: String = "",
    isPasswordTextField: Boolean = false,
    isPasswordShown: Boolean = true,
    onPasswordVisibilityChange: () -> Unit = {},
    textStyle: TextStyle = TextStyle(color = Color.Black),
    onChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    testTagSemantic: String = "",
) {
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        Box {
            BasicTextField(
                visualTransformation = if (isPasswordShown) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, MaterialTheme.shapes.medium)
                    .background(WhitePlain, MaterialTheme.shapes.medium)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .onFocusChanged {
                        isHintDisplayed = !it.isFocused
                    }
                    .semantics {
                        testTag = testTagSemantic
                    },
                value = text,
                maxLines = 1,
                singleLine = true,
                enabled = isEnable,
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    onChange(it)
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
            if (isPasswordTextField) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onPasswordVisibilityChange) {
                        if (isPasswordShown) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_visibility_24),
                                contentDescription = null,
                                tint = BlackTypo
                            )
                        } else {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_visibility_off_24),
                                contentDescription = null,
                                tint = BlackTypo
                            )
                        }
                    }
                }
            }
            if (isHintDisplayed && text == "") {
                Text(
                    text = hint,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Inter,
                    color = WhitePlaceholder
                )
            }
        }
        if (errorMessage != "") {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = RedDanger
            )
        }
    }
}
