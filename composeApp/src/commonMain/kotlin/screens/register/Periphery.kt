package screens.register

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.ColorAssets
import components.LMTextFiled
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.EyeOff
import data.SpecificConfiguration
import data.resources.Poppins
import data.resources.TermConditions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import org.jetbrains.compose.resources.painterResource

data class ColorFullyShapeData(val color: Color, val rotate: Float, val scale: Float, val alpha: Float, val modifier: Modifier)

enum class TextFiledInputStatus {
    ERROR, CORRECT, DEFAULT;
}

class ValidationText(
    initialValue: String,
    private val regex: Regex,
    private val onValueChange: (String) -> Unit = { }
) {
    val mutableState: MutableState<String> = mutableStateOf(initialValue)

    var status: TextFiledInputStatus = if (regex.matches(initialValue)) {
        TextFiledInputStatus.CORRECT
    } else {
        TextFiledInputStatus.ERROR
    }

    val isCertain: Boolean get() = (status == TextFiledInputStatus.CORRECT)

    fun updateStatus(value: String) {
        onValueChange.invoke(value)
        status = if (regex.matches(value)) {
            TextFiledInputStatus.CORRECT
        } else {
            TextFiledInputStatus.ERROR
        }
    }
}

internal val SignatureColorList get() = listOf(
    Color(0xFF570BFA), // Purple
    Color(0xFFE6E34A), // Yellow
    Color(0xFF4A9DE6), // Blue
    Color(0xFFEC8CDA) // Pink
)

@Composable
internal fun multiColorBackground() {
    val hazeState = remember { HazeState() }
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    val shapeList = listOf(
        ColorFullyShapeData(SignatureColorList[0], -19f, 1.6f, 0.42f, Modifier.haze(hazeState)), // Purple
        ColorFullyShapeData(SignatureColorList[1], 17f, 0.9f, 0.3f, Modifier.offset(screenSize.width * 0.2f, screenSize.height * 0.6f).haze(hazeState)), // Yellow
        ColorFullyShapeData(SignatureColorList[2], -17f, 1.2f, 0.2f, Modifier.offset(-screenSize.width * 0.2f, screenSize.height * 0.4f).haze(hazeState)), // Blue
        ColorFullyShapeData(SignatureColorList[3], 21.5f, 1f, 0f, Modifier.offset(screenSize.width * 0.3f, screenSize.height * 0.2f).haze(hazeState)) // Pink
    )
    val hazeColor = MaterialTheme.colors.surface
    val hazeBackgroundAlpha = 0.3f
    val blur = 24.dp

    Box(
        modifier = Modifier
            .hazeChild(hazeState, style = HazeStyle(hazeColor.copy(alpha = hazeBackgroundAlpha), blurRadius = blur))
            .fillMaxSize()
    ) { shapeList.forEach { colorfullyShape(it) } }
}

@Composable
internal fun termsAndCondition() {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(SpecificConfiguration.defaultContentPadding),
    ) {
        item {
            Text(TermConditions.TITLE, style = MaterialTheme.typography.h6)

            Text(TermConditions.EFFECTIVE_DATE, style = MaterialTheme.typography.h6)

            Text(text = TermConditions.INTRODUCTION, style = MaterialTheme.typography.body2, modifier = Modifier.padding(0.dp, 8.dp))
        }

        items(TermConditions.TERMS.size) {
            Text(TermConditions.TERMS[it].title, style = MaterialTheme.typography.h6)

            TermConditions.TERMS[it].content.forEachIndexed { index, s ->
                Text("$index. ${s + 1}", style = MaterialTheme.typography.body2, modifier = Modifier.padding(0.dp, 8.dp))
            }
        }
    }
}

@Composable
internal fun contentFramework(phase: RegisterPhase, block: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxHeight().weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(phase.drawableResource),
                contentDescription = phase.drawableResource.toString(),
                modifier = Modifier.size(192.dp)
            )
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(phase.titlePair.first, style = MaterialTheme.typography.h4, fontFamily = Poppins.semiBold.toFontFamily())

            phase.titlePair.second?.let {
                Text(it, style = MaterialTheme.typography.subtitle2, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(0.8f))
            }
        }

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            block()
        }
    }
}

@Composable
internal fun colorfullyShape(data: ColorFullyShapeData) {
    Box(
        modifier = data.modifier
            .rotate(data.rotate)
            .size(SpecificConfiguration.localScreenConfiguration.bounds)
            .scale(data.scale)
            .clip(RoundedCornerShape(40))
            .background(
                brush = Brush.radialGradient(listOf(data.color, data.color.copy(alpha = data.alpha))),
                shape = RoundedCornerShape(40)
            )
    ) { }
}

@Composable
internal fun continueButton(leading: @Composable () -> Unit, name: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colors.surface) // MaterialTheme.colors.surface
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leading()

        Text(name, style = MaterialTheme.typography.body2, fontWeight = FontWeight.Bold)
    }
}

@Composable
internal fun largeButton(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colors.onSurface)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.surface
        )
    }
}

@Composable
internal fun checkablePrivacyTextArea(
    state: ValidationText,
    placeholder: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    privacy: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    statusChange: ((String, TextFiledInputStatus) -> TextFiledInputStatus)? = null
) {
    var show by remember { mutableStateOf(!privacy) }
    var onFocus by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(TextFiledInputStatus.DEFAULT) }
    val borderColor = animateColorAsState(
        targetValue = when (status)  {
            TextFiledInputStatus.DEFAULT -> Color.Transparent
            TextFiledInputStatus.ERROR -> ColorAssets.Red.value
            TextFiledInputStatus.CORRECT -> ColorAssets.Green.value
        },
        animationSpec = tween(400)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colors.surface)
            .border(2.dp, borderColor.value, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LMTextFiled(
            text = state.mutableState.value,
            onValueChange = { newValue ->
                state.mutableState.value = newValue
                state.updateStatus(newValue)
                status = statusChange?.let { it(newValue, state.status) } ?: state.status
            },
            textStyle = MaterialTheme.typography.body2.copy(
                fontFamily = Poppins.regular.toFontFamily(),
                color = MaterialTheme.colors.onSurface
            ),
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = if (!show) keyboardOptions.copy(keyboardType = KeyboardType.Password) else keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = if (!show) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier.fillMaxWidth().weight(1f),
            placeholder = placeholder,
            onFocusChange = { onFocus = it }
        )

        if (privacy) {
            Icon(
                if (show) EvaIcons.Outline.Eye else EvaIcons.Outline.EyeOff,
                contentDescription = "Show or hide privacy content.",
                modifier = Modifier.size(18.dp).clickable { show = !show }
            )
        }
    }
}