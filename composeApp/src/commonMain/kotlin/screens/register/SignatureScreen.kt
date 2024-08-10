package screens.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.CircleCheckbox
import components.ColorAssets.LightGray
import components.ColorSet
import components.LMTextFiled
import components.Link
import components.screens.MuseumPaging
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.EyeOff
import data.SpecificConfiguration
import data.Zero
import data.modules.getViewModel
import data.resources.Poppins
import data.resources.TermConditions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.emoji_camera_animated
import lifemark_kmm.composeapp.generated.resources.emoji_party_animated
import lifemark_kmm.composeapp.generated.resources.emoji_partying_animated
import lifemark_kmm.composeapp.generated.resources.emoji_thinking_animated
import lifemark_kmm.composeapp.generated.resources.media_apple
import lifemark_kmm.composeapp.generated.resources.media_email
import lifemark_kmm.composeapp.generated.resources.media_google
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import screens.MainApplicationNavigator
import screens.merge.SignUp
import viewmodel.SnapAlertViewModel

private enum class RegisterPhase {
    PROCESS_JOIN {
        override val drawableResource get() = Res.drawable.emoji_camera_animated

        override val titlePair: Pair<String, String> = Pair("Join community", "Now your finances are in one place and always under control")
    },
    REGISTER {
        override val drawableResource get() = Res.drawable.emoji_partying_animated

        override val titlePair: Pair<String, String?> = Pair("Create account", null)
    },
    SIGN_IN {
        override val drawableResource get() = Res.drawable.emoji_party_animated

        override val titlePair: Pair<String, String?> = Pair("Welcome back", null)
    };

    abstract val drawableResource: DrawableResource

    abstract val titlePair: Pair<String, String?>
}

object SignatureScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // MARK: Signature view model
        val viewModel: SignatureViewModel = getViewModel()

        // Haze style and states
        val hazeStyle = HazeStyle(blurRadius = 18.dp)
        val hazeState = remember { HazeState() }

        val switchAnimationSpec = spring<Float>(Spring.DampingRatioLowBouncy, Spring.StiffnessVeryLow)

        // Term condition information states
        var showTermCondition by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        // Sign phase states
        var processPhase by remember { mutableStateOf(RegisterPhase.PROCESS_JOIN) }
        val pagerState = rememberPagerState { RegisterPhase.entries.size }
        LaunchedEffect(processPhase) {
            pagerState.animateScrollToPage(processPhase.ordinal, animationSpec = switchAnimationSpec)
        }

        // viewModel state
        val state = viewModel.state.value
        when (state.success) {
            0 -> {}

            200 -> {
                LaunchedEffect(key1 = Unit) {
                    navigator.push(MuseumPaging()) }
            }

            400 -> {
                LaunchedEffect(key1 = Unit) {
                    //navigator.push(SignUp())
                    pagerState.animateScrollToPage(RegisterPhase.REGISTER.ordinal, animationSpec = switchAnimationSpec)

                }
            }

        }

        // The way process to third part sign way...
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        fun thirdPartCall(onFailure: suspend () -> Unit) {
            scope.launch {
                isLoading = true
                delay(2000L)
                isLoading = false
                onFailure()
            }
        }

        // Animated by system dark mode and drive in `AnimatedVisibility`
        @Composable
        fun animatedJoinPhaseBottomConditions() {
            AnimatedVisibility(
                visible = isSystemInDarkTheme(),
                enter = fadeIn(switchAnimationSpec) + expandVertically(),
                exit = fadeOut(switchAnimationSpec) + shrinkVertically(),
            ) {
                Spacer(Modifier.height(8.dp))

                Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                    Link("Already have account? ", "Log in") { processPhase = RegisterPhase.SIGN_IN }

                    Link("", "Entry without sign") { navigator.replaceAll(MainApplicationNavigator) }
                }
            }
        }

        // Framework animation states
        val animatedContainerRounded = animateDpAsState(if (isSystemInDarkTheme()) 0.dp else 36.dp, tween(400))
        // Out
        val animatedOutContainerHorizontalPadding = animateDpAsState(if (isSystemInDarkTheme()) 0.dp else SpecificConfiguration.defaultContentPadding, tween(400))
        val animatedOutContainerBottomPadding = animateDpAsState(if (isSystemInDarkTheme()) 0.dp else SpecificConfiguration.defaultContentPadding + 8.dp, tween(400))
        val animatedOutContainerTopPadding = animateDpAsState(if (isSystemInDarkTheme()) 0.dp else SpecificConfiguration.edgeSafeArea.asPaddingValues().calculateTopPadding(), tween(400))
        // Inner
        val animatedInnerContainerHorizontalPadding = animateDpAsState(if (isSystemInDarkTheme()) SpecificConfiguration.defaultContentPadding else 0.dp, tween(400))
        val animatedInnerContainerBottomPadding = animateDpAsState(if (isSystemInDarkTheme()) SpecificConfiguration.defaultContentPadding + 8.dp else 0.dp, tween(400))
        val animatedInnerContainerTopPadding = animateDpAsState(if (isSystemInDarkTheme()) SpecificConfiguration.edgeSafeArea.asPaddingValues().calculateTopPadding() else 0.dp, tween(400))

        // UI
        Surface(if (isLoading) Modifier.hazeChild(hazeState, style = hazeStyle).clickable(onClick = { }, enabled = false) else Modifier) {
            Column(
                modifier = Modifier
                    .haze(hazeState)
                    .padding(top = animatedOutContainerTopPadding.value) // EXPIRED: .statusBarsPadding()
                    .padding(bottom = animatedOutContainerBottomPadding.value) // EXPIRED: SpecificConfiguration.defaultContentPadding + 8.dp
                    .padding(horizontal = animatedOutContainerHorizontalPadding.value) // EXPIRED: SpecificConfiguration.defaultContentPadding
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clip(RoundedCornerShape(animatedContainerRounded.value)) // EXPIRED: 36.dp
                        .border(2.dp, ColorSet(LightGray.default, Color.Transparent).value, RoundedCornerShape(animatedContainerRounded.value)) // 36.dp
                ) {
                    multiColorBackground()

                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false,
                        verticalAlignment = Alignment.CenterVertically,
                        pageSpacing = SpecificConfiguration.defaultContentPadding,
                        modifier = Modifier
                            .padding(top = animatedInnerContainerTopPadding.value)
                            .padding(bottom = animatedInnerContainerBottomPadding.value)
                            .padding(horizontal = animatedInnerContainerHorizontalPadding.value)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) { index ->
                        when(RegisterPhase.entries[index]) {
                            RegisterPhase.PROCESS_JOIN -> contentFramework(RegisterPhase.PROCESS_JOIN) {
                                continueButton({
                                    Image(painterResource(Res.drawable.media_google), null, Modifier.size(24.dp))
                                }, "Continue with Google") {
                                    thirdPartCall { SnapAlertViewModel.push("Google sign is be in progress") }
                                }

                                continueButton({
                                    Icon(painterResource(Res.drawable.media_apple), null, Modifier.size(24.dp), tint = MaterialTheme.colors.onSurface)
                                }, "Continue with Apple") {
                                    thirdPartCall { SnapAlertViewModel.push("Apple sign is be in progress") }
                                }

                                continueButton({
                                    Icon(painterResource(Res.drawable.media_email), null, Modifier.size(24.dp), tint = MaterialTheme.colors.onSurface)
                                }, "Continue with Email") {
                                    processPhase = RegisterPhase.REGISTER
                                }

                                animatedJoinPhaseBottomConditions()
                             }

                            RegisterPhase.REGISTER -> contentFramework(RegisterPhase.REGISTER) {
                                var email: String by remember { mutableStateOf("") }
                                var password: String by remember { mutableStateOf("") }
                                var confirmPassword: String by remember { mutableStateOf("") }

                                checkablePrivacyTextArea(email, { email = it }, "Email...")

                                checkablePrivacyTextArea(password, { password = it }, "Password", privacy = true)

                                checkablePrivacyTextArea(confirmPassword, { confirmPassword = it }, "Conform the password", privacy = true)

                                Spacer(Modifier.height(18.dp))

                                largeButton("Create account") { viewModel.register(email, password, confirmPassword) }

                                Spacer(Modifier.height(8.dp))

                                Column(Modifier.fillMaxWidth(), Arrangement.Center ,Alignment.CenterHorizontally) {
                                    Link("Agree our ", "Terms and Conditions") { showTermCondition = true }

                                    Link("Already have an account? ", "Log in") { processPhase = RegisterPhase.SIGN_IN }
                                }
                            }

                            RegisterPhase.SIGN_IN -> contentFramework(RegisterPhase.SIGN_IN) {
                                var email: String by remember { mutableStateOf("") }
                                var password: String by remember { mutableStateOf("") }
                                var rememberMe: Boolean by remember { mutableStateOf(false) }

                                checkablePrivacyTextArea(email, { email = it }, "Email address")

                                checkablePrivacyTextArea(password, { password = it }, "Password", privacy = true)

                                Row(Modifier.padding(horizontal = 12.dp).fillMaxWidth(), Arrangement.spacedBy(6.dp), Alignment.CenterVertically) {
                                    CircleCheckbox(rememberMe) { rememberMe = it }

                                    Text(
                                        text = "Remember me",
                                        style = MaterialTheme.typography.subtitle2,
                                        modifier = Modifier.clickable(onClick = { rememberMe = !rememberMe }, indication = null, interactionSource = remember { MutableInteractionSource() })
                                    )

                                    Spacer(Modifier.weight(1f))

                                    Text("Forgot password?", style = MaterialTheme.typography.subtitle2)
                                }

                                Spacer(Modifier.height(50.dp))

                                largeButton("Log in") { viewModel.login(email, password) }

                                Spacer(Modifier.height(8.dp))

                                Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {

                                    Link("Do not have an account? ", "Register") { processPhase = RegisterPhase.REGISTER }

                                    Link("", "Sign with third part way") { processPhase = RegisterPhase.PROCESS_JOIN }
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = processPhase == RegisterPhase.PROCESS_JOIN && !isSystemInDarkTheme(),
                    enter = fadeIn(switchAnimationSpec) + expandVertically(),
                    exit = fadeOut(switchAnimationSpec) + shrinkVertically(),
                ) {
                    Column(Modifier.fillMaxWidth().padding(top = 18.dp).navigationBarsPadding(), Arrangement.Center, Alignment.CenterHorizontally) {
                        Link("Already have account? ", "Log in") { processPhase = RegisterPhase.SIGN_IN }

                        Link("", "Entry without sign") { navigator.replaceAll(MainApplicationNavigator) }
                    }
                }
            }

            Box(Modifier.alpha(if (isLoading) 1f else 0f).fillMaxSize(), Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colors.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Image(painterResource(Res.drawable.emoji_thinking_animated), "emoji_thinking_animated", modifier = Modifier.size(28.dp))

                    CircularProgressIndicator(modifier = Modifier.size(64.dp), color = SignatureColorList.random(), strokeWidth = 3.dp, strokeCap = StrokeCap.Round)
                }
            }
        }

        if (showTermCondition) {
            ModalBottomSheet(
                onDismissRequest = { showTermCondition = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colors.background,
                windowInsets = WindowInsets.Zero
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(SpecificConfiguration.defaultContentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { termsAndCondition() }
            }
        }
    }
}

/// ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
/// ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————

data class ColorFullyShapeData(val color: Color, val rotate: Float, val scale: Float, val alpha: Float, val modifier: Modifier)

private val SignatureColorList get() = listOf(
    Color(0xFF570BFA), // Purple
    Color(0xFFE6E34A), // Yellow
    Color(0xFF4A9DE6), // Blue
    Color(0xFFEC8CDA) // Pink
)

@Composable
private fun multiColorBackground() {
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
private fun termsAndCondition() {
    Surface {
        LazyColumn {
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
}

@Composable
private fun contentFramework(phase: RegisterPhase, block: @Composable () -> Unit) {
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
private fun colorfullyShape(data: ColorFullyShapeData) {
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
private fun continueButton(leading: @Composable () -> Unit, name: String, onClick: () -> Unit) {
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
private fun largeButton(text: String, onClick: () -> Unit) {
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
private fun checkablePrivacyTextArea(
    text: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    privacy: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var show by remember { mutableStateOf(!privacy) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LMTextFiled(
            text = text,
            onValueChange = onValueChange,
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
        )

        if (privacy) {
            Icon(
                if (show) EvaIcons.Outline.Eye else EvaIcons.Outline.EyeOff,
                contentDescription = null,
                modifier = Modifier.size(18.dp).clickable { show = !show }
            )
        }
    }
}