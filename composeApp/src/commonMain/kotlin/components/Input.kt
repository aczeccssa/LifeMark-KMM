package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Checkmark
import compose.icons.evaicons.outline.Close

@Composable
fun LMTextFiled(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    iconSpacing: Dp = 6.dp,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: Brush = SolidColor(MaterialTheme.colors.primary),
) {
    var hasFocus by remember { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { hasFocus = it.isFocused },
        singleLine = true,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        cursorBrush = cursorBrush,
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier.weight(1f)) {
                    if (text.isEmpty())
                        Text(
                            text = placeholder,
                            color = ColorAssets.Gray.value,
                            style = textStyle
                        )
                    innerTextField()
                }

                if (hasFocus && text.isNotEmpty()) {
                    Image(imageVector = EvaIcons.Outline.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable { onValueChange.invoke("") }
                    )
                }
            }
        }
    )
}


@Composable
fun CircleCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    var checkedState by remember { mutableStateOf(checked) }
    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    checkedState = !checkedState
                    onCheckedChange(checkedState)
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .size(20.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = if (checked) 1f else 0f))
            .border(2.dp, MaterialTheme.colors.onSurface, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = EvaIcons.Outline.Checkmark,
            contentDescription = null,
            tint = MaterialTheme.colors.surface.copy(alpha = if (checked) 1f else 0f),
            modifier = Modifier.fillMaxSize(0.8f)
        )
    }
}

@Composable
fun Link(lead: String, key: String, onClick: () -> Unit) {
    Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
        Text(lead, style = MaterialTheme.typography.subtitle2)
        Text(
            text = key,
            style = MaterialTheme.typography.subtitle2,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onClick() }
        )
    }
}