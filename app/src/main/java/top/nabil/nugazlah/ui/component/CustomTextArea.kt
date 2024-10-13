package top.nabil.nugazlah.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.nabil.nugazlah.ui.theme.Inter
import top.nabil.nugazlah.ui.theme.WhitePlaceholder
import top.nabil.nugazlah.ui.theme.WhitePlain


@Composable
fun CustomTextArea(
    modifier: Modifier = Modifier,
    hint: String = "",
    isEnable: Boolean = true,
    text: String = "",
    textStyle: TextStyle = TextStyle(color = Color.Black),
    height: Dp = 200.dp,
    onChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    testTagSemantic: String = ""
) {
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
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
            )
        )
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
}
