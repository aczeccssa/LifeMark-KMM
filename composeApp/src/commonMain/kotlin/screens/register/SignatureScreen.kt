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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.CircleCheckbox
import components.ColorAssets.LightGray
import components.ColorSet
import components.Link
import data.SpecificConfiguration
import data.Zero
import data.modules.getViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.emoji_thinking_animated
import lifemark_kmm.composeapp.generated.resources.media_apple
import lifemark_kmm.composeapp.generated.resources.media_email
import lifemark_kmm.composeapp.generated.resources.media_google
import org.jetbrains.compose.resources.painterResource
import screens.MainApplicationNavigator
import viewmodel.SnapAlertViewModel

object SignatureScreen : Screen {
    val processPhaseStatus = mutableStateOf(RegisterPhase.PROCESS_JOIN)
    val switchAnimationSpec = spring<Float>(Spring.DampingRatioLowBouncy, Spring.StiffnessVeryLow)

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // MARK: Signature view model
        val viewModel: SignatureViewModel = getViewModel()

        // Haze style and states
        val hazeStyle = HazeStyle(blurRadius = 18.dp)
        val hazeState = remember { HazeState() }

        // Term condition information states
        val showTermCondition = remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        // The way process to third part sign way...
        val isLoading = remember { mutableStateOf(false) }

        // Sign phase states
        var processPhase by remember { processPhaseStatus }
        val pagerState = rememberPagerState { RegisterPhase.entries.size }
        LaunchedEffect(processPhase) {
            pagerState.animateScrollToPage(processPhase.ordinal, animationSpec = switchAnimationSpec)
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
        Surface(if (isLoading.value) Modifier.hazeChild(hazeState, style = hazeStyle).clickable(onClick = { }, enabled = false) else Modifier) {
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
                            RegisterPhase.PROCESS_JOIN -> contentFramework(RegisterPhase.PROCESS_JOIN) { joinScreen(isLoading) }

                            RegisterPhase.REGISTER -> contentFramework(RegisterPhase.REGISTER) { registerScreen(viewModel, showTermCondition) }

                            RegisterPhase.SIGN_IN -> contentFramework(RegisterPhase.SIGN_IN) { signInScreen(viewModel) }
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

            Box(Modifier.alpha(if (isLoading.value) 1f else 0f).fillMaxSize(), Alignment.Center) {
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

            if (showTermCondition.value) {
                ModalBottomSheet(
                    onDismissRequest = { showTermCondition.value = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colors.background,
                    // MARK: New alpha haze(material) version change the material dependencies made
                    //  the `ModalBottomSheet` and render of haze effective has changed, we designed
                    //  to no longer upgrade and stay the haze version in 0.7.2.
                    // MARK: New version's haze made `ModalBottomSheet` changed the argument
                    //  from `windowInsets: WindowInsets` to `contentWindowInsets: () -> WindowInsets`
                    windowInsets = WindowInsets.Zero
                ) {
                    Surface {
                        termsAndCondition()
                    }
                }
            }
        }
    }
}

@Composable
internal fun SignatureScreen.joinScreen(isLoading: MutableState<Boolean>) {
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()
    fun thirdPartCall(onFailure: suspend () -> Unit) {
        scope.launch {
            isLoading.value = true
            delay(2000L)
            isLoading.value = false
            onFailure()
        }
    }

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
        processPhaseStatus.value = RegisterPhase.REGISTER
    }

    // Animated by system dark mode and drive in `AnimatedVisibility`
    AnimatedVisibility(
        visible = isSystemInDarkTheme(),
        enter = fadeIn(switchAnimationSpec) + expandVertically(),
        exit = fadeOut(switchAnimationSpec) + shrinkVertically(),
    ) {
        Spacer(Modifier.height(8.dp))

        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            Link("Already have account? ", "Log in") { processPhaseStatus.value = RegisterPhase.SIGN_IN }

            Link("", "Entry without sign") { navigator.replaceAll(MainApplicationNavigator) }
        }
    }
}

@Composable
internal fun SignatureScreen.registerScreen(viewModel: SignatureViewModel, showTermCondition: MutableState<Boolean>) {
    val email = ValidationText("", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex())
    val password = ValidationText("", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-_@])[A-Za-z\\d-_@=]{8,}\$".toRegex())
    val confirmPassword = ValidationText("", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-_@])[A-Za-z\\d-_@=]{8,}\$".toRegex())

    checkablePrivacyTextArea(email,  "Email...")

    checkablePrivacyTextArea(password, "Password", privacy = true)

    checkablePrivacyTextArea(confirmPassword, "Conform the password", privacy = true) { newValue, default ->
        if (newValue != password.mutableState.value) TextFiledInputStatus.ERROR else default
    }

    Spacer(Modifier.height(18.dp))

    largeButton("Create account") {
        if (email.isCertain) {
            if (password.isCertain && confirmPassword.isCertain) {
                if (password.mutableState.value == confirmPassword.mutableState.value) {
                    viewModel.register(email.mutableState.value, password.mutableState.value)
                } else SnapAlertViewModel.push("Password not same")
            } else SnapAlertViewModel.push("Password should constain upper case and lower case letter.")
        } else SnapAlertViewModel.push("email is not correct.")
    }

    Spacer(Modifier.height(8.dp))

    Column(Modifier.fillMaxWidth(), Arrangement.Center ,Alignment.CenterHorizontally) {
        Link("Agree our ", "Terms and Conditions") { showTermCondition.value = true }

        Link("Already have an account? ", "Log in") { processPhaseStatus.value = RegisterPhase.SIGN_IN }
    }
}

@Composable
internal fun SignatureScreen.signInScreen(viewModel: SignatureViewModel) {
    val email = ValidationText("", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex())
    val password = ValidationText("", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-_@])[A-Za-z\\d-_@=]{8,}\$".toRegex())
    var rememberMe: Boolean by remember { mutableStateOf(false) }

    checkablePrivacyTextArea(email, "Email...")

    checkablePrivacyTextArea(password, "Password", privacy = true)

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

    largeButton("Log in") {
        if (email.isCertain) {
            if (password.isCertain) {
                viewModel.login(email.mutableState.value, password.mutableState.value)
            } else SnapAlertViewModel.push("Password is not correct.")
        } else SnapAlertViewModel.push("email is not correct.")
    }

    Spacer(Modifier.height(8.dp))

    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Link("Do not have an account? ", "Register") { processPhaseStatus.value = RegisterPhase.REGISTER }

        Link("", "Sign with third part way") { processPhaseStatus.value = RegisterPhase.PROCESS_JOIN }
    }
}