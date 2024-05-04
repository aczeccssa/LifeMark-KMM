package screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader


object ExperimentalAnimate : Screen {
    @Composable
    override fun Content() {
//        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp
//        val canvasSizeValue = Size(100f, 100f)
//        val coroutineScope = rememberCoroutineScope()
//        val ballStates = remember { mutableStateListOf<BallState>() }
//        var canvasSize by remember { mutableStateOf(canvasSizeValue) } // Store canvas size
//        var shouldInitializeBalls by remember { mutableStateOf(true) } // 新增状态用于控制是否初始化小球

//        LaunchedEffect(Unit) {
//            coroutineScope.launch {
//                val ballCount = globalRandom.nextInt(50) + 1
//                repeat(ballCount) {
//                    ballStates.add(
//                        BallState(
//                            id = it,
//                            position = Offset(
//                                x = globalRandom.nextFloat() * canvasSize.width,
//                                y = globalRandom.nextFloat() * canvasSize.height
//                            ),
//                            velocity = Offset(
//                                x = (globalRandom.nextFloat() - globalRandom.nextFloat()) * 50f,
//                                y = (globalRandom.nextFloat() - globalRandom.nextFloat()) * 50f
//                            ),
//                            lifetime = globalRandom.nextLong() + 3000L // 3 seconds lifetime
//                        )
//                    )
//                }
//                shouldInitializeBalls = false // 初始化后设置为false，避免重复初始化
//                while (ballStates.isNotEmpty()) {
//                    delay(16L) // Approximate 60 FPS
//                    val currentTime = Clock.System.now().toEpochMilliseconds()
//                    ballStates.removeAll { currentTime > it.lifetime }
//                }
//            }
//        }

        Surface {
            NavigationHeader("Animations")

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            ballStates.clear() // Clear existing balls
//                            shouldInitializeBalls = true // Allow reinitialization
//                        }
//                    },
//                    modifier = Modifier.align(Alignment.Center)
//                ) {
//                    Text("Click to Restart Animation")
//                }
//                if (ballStates.isNotEmpty()) {
//                    DrawMovingBalls(ballStates, onSizeChanged = { newSize ->
//                        canvasSize = newSize
//                    })
//                }
            }
        }
    }
}
//@Composable
//private fun DrawMovingBalls(ballStates: List<BallState>, onSizeChanged: (Size) -> Unit) {
//
//
//
//            ballStates += BallState(/* 初始化位置 */, /* 初始化速度 */)
//
//
//
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        onSizeChanged(size)
//
//        if (size != Size.Unspecified) {
//            ballStates.forEach { ball ->
//                drawCircle(color = Color.Blue, radius = 20f, center = ball.position)
//            }
//        }
//    }
//}
//
//val random = Random(Clock.System.now().toEpochMilliseconds())
//val rangeFactor = 0.5f // 这意味着小球将在屏幕的中心区域，占半个屏幕的宽度和高度
//
//@Composable
//fun RandomBallGenerator() {
//    val coroutineScope = rememberCoroutineScope()
//    val ballStates = remember { mutableStateListOf<BallState>() }
//    var canvasSize by remember { mutableStateOf(Size.Unspecified) } // Store canvas size
//
//    LaunchedEffect(true) {
//        coroutineScope.launch {
//            val ballCount = random.nextInt(50) + 1
//            repeat(ballCount) {
//                ballStates.add(
//                    BallState(
//                        id = it,
//                        position = Offset(
//                            x = random.nextFloat() * 0.5f * it,
//                            y = random.nextFloat() * 0.5f * it
//                        ),
//                        velocity = Offset(
//                            x = (random.nextFloat() - random.nextFloat()) * 50f,
//                            y = (random.nextFloat() - random.nextFloat()) * 50f
//                        ),
//                        lifetime = random.nextLong() + 3000 // Each ball lasts for 3 seconds
//                    )
//                )
//                delay(1000)
//            }
//            // Animation loop to update ball positions
//            while (ballCount > 0) {
//                delay(16L) // Approximate 60 FPS
//
//                // Use remembered canvasSize here
//                val boundsPadding = 20f
//                ballStates.forEachIndexed { index, ball ->
//                    val newPosition = ball.position + ball.velocity
//                    val boundedPosition = Offset(
//                        x = newPosition.x.coerceIn(boundsPadding, canvasSize.width - boundsPadding),
//                        y = newPosition.y.coerceIn(boundsPadding, canvasSize.height - boundsPadding)
//                    )
//                    ballStates[index] = ball.copy(position = boundedPosition)
//                }
//            }
//            ballStates.clear() // Reset after animation ends
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Button(
//            onClick = {
//                coroutineScope.launch {
//                    ballStates.clear()
//                    delay(3000)
//                }
//            },
//            modifier = Modifier.align(Alignment.Center)
//        ) {
//            Text("Click to Restart Animation")
//        }
//
//        if (ballStates.isNotEmpty()) {
//            DrawMovingBalls(ballStates) { canvasSize = it }
//        }
//    }
//}
//data class Particle(
//    val offset: Offset,
//    val color: Color,
//    val velocity: Offset,
//    val lifeSpan: Float = 3f, // 粒子存活时间，单位秒
//    var age: Float = 0f // 粒子当前存活时间
//)
//
//val particleCount = 100
//val particleRadius = 5.dp
//
//@Composable
//fun ParticleEffectCanvas(modifier: Modifier = Modifier) {
//    val particles by remember { mutableStateOf(mutableStateListOf<Particle>()) }
//    val screenSize = DpSize(500.dp, 500.dp)// SpecificConfiguration.localScreenConfiguration.bounds
//    val screenWidth = screenSize.width
//    val screenHeight = screenSize.height
//    val currentDensity = LocalDensity.current
//
//    LaunchedEffect(Unit) {
//        println("Started!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
//    }
//
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier
//                .width(screenWidth)
//                .height(screenHeight)
//                .padding(16.dp)
//        ) {
//            Canvas(modifier = Modifier.matchParentSize()) {
//                particles.forEach { particle ->
//                    println("Current number: ${particles.size}")
//                    drawCircle(
//                        color = particle.color,
//                        center = particle.offset,
//                        radius = with(currentDensity) { particleRadius.toPx() }
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                val centerX = with(currentDensity) { screenWidth.toPx() / 2 }
//                val centerY = with(currentDensity) { screenHeight.toPx() / 2 }
//                particles.clear() // 清空现有粒子
//                particles.addAll(generateParticles(particleCount, centerX, centerY))
//            },
//            shape = CircleShape,
//            modifier = Modifier.padding(horizontal = 16.dp)
//        ) {
//            Text("Generate Particles")
//        }
//    }
//
//    LaunchedEffect(key1 = particles) {
//        while (true) {
//            val updatedParticles = particles.map { updateParticle(it, screenSize) }
//                .filterNotNull() // 过滤掉生命周期结束的粒子
//            particles.clear()
//            particles.addAll(updatedParticles)
//            delay(16) // 为了更流畅的动画，可以尝试更高的帧率，例如每16ms更新一次
//        }
//    }
//}
//
//fun generateParticles(count: Int, centerX: Float, centerY: Float): List<Particle> {
//    val speedFactor = 2f // 控制粒子向外扩散的速度
//    return List(count) {
//        val initOffset = generateRandomOffsetAroundCenter(centerX, centerY, 50f) // 初始位置可以有小范围偏移
//        val direction = initOffset - Offset(centerX, centerY) // 计算每个粒子初始位置到中心点的方向向量
//        val normalizedDirection = direction / direction.length() * speedFactor // 标准化方向向量并赋予速度
//        Particle(
//            offset = initOffset,
//            color = StaticColorList.random(),
//            velocity = normalizedDirection // 使用这个方向作为速度，确保粒子向外扩散
//        )
//    }
//}
//
//fun updateParticle(particle: Particle, size: DpSize): Particle {
//    val newOffset = particle.offset + particle.velocity
//    // 确保粒子在画布边界内
//    val boundedOffset = Offset(
//        x = newOffset.x.coerceIn(0f, size.width.value.toFloat()),
//        y = newOffset.y.coerceIn(0f, size.height.value.toFloat())
//    )
//    // 更新年龄并检查生命周期
//    val updatedAge = particle.age + 1f / 60f
//
//    if (updatedAge >= particle.lifeSpan) {
//        null
//    } else {
//        particle.copy(offset = boundedOffset, age = updatedAge)
//    }
//}
//private val StaticColorList = listOf(
//    Color(0xFF2d6359),
//    Color(0xFFe388b1),
//    Color(0xFF32776a),
//    Color(0xFFf4d348),
//    Color(0xFF268b4c),
//    Color(0xFF37799e),
//    Color(0xFF459763),
//    Color(0xFFbb3e39),
//)
//
//
//fun generateRandomOffsetAroundCenter(centerX: Float, centerY: Float, maxOffset: Float = 100f): Offset {
//
//    val offsetX = (Random.nextFloat() * 4 * maxOffset).toInt().toFloat() - maxOffset
//    val offsetY = (Random.nextFloat() * 6 * maxOffset).toInt().toFloat() - maxOffset
//
//    return Offset(
//        x = centerX + offsetX,
//        y = centerY + offsetY
//    )
//}



// ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
// ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
// ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
// ————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//@Composable
//fun MovingParticle(
//    center: Offset,
//    pathLength: Float,
//    color: Color,
//    lifeSpanSeconds: Float,
//    ageSeconds: Float,
//    modifier: Modifier = Modifier
//) {
//    Canvas(modifier = modifier) {
//        drawCircle(color = color, radius = 10.dp.toPx(), center = center)
//    }
//}
//
//@Composable
//fun MovingParticles() {
//    val particles = remember {
//        mutableStateListOf<Particle>()
//    }
//
//    LaunchedEffect(key1 = Unit) {
//        val random = Random(Clock.System.now().toEpochMilliseconds()) // currentTimeMillis()
//        val randomDest = Random.nextInt(200)..400
//        repeat(100) { // 初始化100个粒子
//            particles.add(
//                Particle(
//                    initialCenter = Offset(randomDest.random().toFloat(), randomDest.random().toFloat()),
//                    pathLength = 300f,
//                    color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
//                    lifeSpan = 3f + random.nextFloat() * 2 // 粒子生命跨度3-5秒
//                )
//            )
//        }
//
//        var startTime = Clock.System.now().toEpochMilliseconds()
//
//        while (true) {
//            val currentTime = Clock.System.now().toEpochMilliseconds()
//            val elapsedTimeMillis = currentTime - startTime
//            startTime = currentTime
//
//            val updatedParticles = particles.map { particle ->
//                updateParticle(particle, elapsedTimeMillis.toFloat() / 1000f)
//            }.filter { it.isAlive }
//
//            particles.clear()
//            particles.addAll(updatedParticles)
//
//            delay(16L) // 控制更新频率
//        }
//    }
//
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        particles.forEach { particle ->
//            MovingParticle(
//                center = particle.initialCenter,
//                pathLength = particle.pathLength,
//                color = particle.color,
//                lifeSpanSeconds = particle.lifeSpan,
//                ageSeconds = particle.age
//            )
//        }
//    }
//}
//
//data class Particle(
//    val initialCenter: Offset,
//    val pathLength: Float,
//    val color: Color,
//    val lifeSpan: Float = 3f,
//    var age: Float = 0f,
//    var isAlive: Boolean = true
//)
//
//fun updateParticle(particle: Particle, elapsedTimeInSeconds: Float): Particle {
//    val timeProgress = particle.age / particle.lifeSpan
//    val newPosition = particle.initialCenter +
//            (particle.pathLength * timeProgress).let {
//                Offset(it, it) // 假设粒子沿对角线移动
//            }
//
//    val updatedAge = particle.age + elapsedTimeInSeconds
//    return particle.copy(
//        initialCenter = newPosition,
//        age = updatedAge,
//        isAlive = updatedAge < particle.lifeSpan
//    )
//}