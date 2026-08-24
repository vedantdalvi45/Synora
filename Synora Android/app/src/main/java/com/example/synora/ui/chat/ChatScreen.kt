package com.example.synora.ui.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.synora.domain.model.ChatMessage
import com.example.synora.domain.model.MessageStatus
import com.example.synora.ui.components.SynoraAvatar
import com.example.synora.ui.theme.Indigo500
import com.example.synora.ui.theme.Indigo600
import com.example.synora.ui.theme.Spacing
import com.example.synora.ui.theme.SynoraTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ── Entry point ───────────────────────────────────────────────────────────────

@Composable
fun ChatScreen(
    onNavigateUp: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ChatScreenContent(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onInputChange = viewModel::onInputChange,
        onSend = viewModel::sendMessage,
    )
}

// ── Root layout ───────────────────────────────────────────────────────────────

@Composable
private fun ChatScreenContent(
    uiState: ChatUiState,
    onNavigateUp: () -> Unit,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    val listState = rememberLazyListState()
    val dotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f)

    LaunchedEffect(uiState.messages.size, uiState.isContactTyping) {
        val target = if (uiState.isContactTyping) uiState.messages.size
                     else (uiState.messages.size - 1).coerceAtLeast(0)
        if (uiState.messages.isNotEmpty()) listState.animateScrollToItem(target)
    }

    // Full-screen Box — draws behind status bar and nav bar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                val spacing = 28.dp.toPx()
                val r = 1.5.dp.toPx()
                var x = 0f
                while (x < size.width) {
                    var y = 0f
                    while (y < size.height) {
                        drawCircle(dotColor, r, Offset(x, y))
                        y += spacing
                    }
                    x += spacing
                }
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),                      // entire column shifts up with keyboard
        ) {
            // ── Top bar (draws into status bar area) ──────────────────────────
            ChatTopBar(
                contactName = uiState.contactName,
                isTyping = uiState.isContactTyping,
                onNavigateUp = onNavigateUp,
            )

            // ── Message list — takes all remaining space ───────────────────────
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                // Bubble max width = 72% of screen width — scales on any device
                val bubbleMaxWidth: Dp = maxWidth * 0.72f

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spacing.sm,
                        end = Spacing.sm,
                        top = Spacing.sm,
                        bottom = Spacing.sm,
                    ),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    reverseLayout = false,
                ) {
                    val grouped = uiState.messages.groupBy { formatDateHeader(it.timestamp) }
                    grouped.forEach { (label, msgs) ->
                        item(key = "hdr_$label") { DateHeader(label) }
                        items(msgs, key = { it.id }) { msg ->
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(tween(160)) + slideInVertically { it / 3 },
                            ) {
                                MessageBubble(message = msg, maxBubbleWidth = bubbleMaxWidth)
                            }
                        }
                    }
                    if (uiState.isContactTyping) {
                        item(key = "typing") {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically { it / 3 },
                                exit = fadeOut(),
                            ) {
                                TypingBubble()
                            }
                        }
                    }
                }
            }

            // ── Input bar — pinned to bottom, above nav bar ────────────────────
            InputBar(
                text = uiState.inputText,
                onTextChange = onInputChange,
                onSend = onSend,
            )

            // Nav bar spacer so input bar sits above system nav
            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
private fun ChatTopBar(
    contactName: String,
    isTyping: Boolean,
    onNavigateUp: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Indigo600, Indigo500)))
            // Extend gradient behind status bar
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(end = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Back button
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }

            // Avatar
            Box {
                SynoraAvatar(displayName = contactName, size = 38.dp)
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E)),
                )
            }

            Spacer(Modifier.width(Spacing.sm))

            // Name + status — takes remaining space
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contactName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                )
                AnimatedContent(
                    targetState = isTyping,
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                    label = "status",
                ) { typing ->
                    Text(
                        text = if (typing) "typing…" else "online",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (typing) Color(0xFFBFDBFE) else Color.White.copy(alpha = 0.75f),
                    )
                }
            }

            // Actions
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Videocam, contentDescription = "Video call", tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Call, contentDescription = "Voice call", tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White)
            }
        }
    }
}

// ── Message bubble ────────────────────────────────────────────────────────────

@Composable
private fun MessageBubble(message: ChatMessage, maxBubbleWidth: Dp) {
    val isMine = message.isMine

    val bubbleBrush = if (isMine) Brush.linearGradient(listOf(Indigo500, Indigo600)) else null
    val bubbleBg    = if (!isMine) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
    val textColor   = if (isMine) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    val metaColor   = if (isMine) Color.White.copy(alpha = 0.65f)
                      else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    val bubbleShape = if (isMine)
        RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp,  bottomStart = 18.dp, bottomEnd = 18.dp)
    else
        RoundedCornerShape(topStart = 4.dp,  topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (!isMine) {
            SynoraAvatar(displayName = "?", size = 26.dp)
            Spacer(Modifier.width(Spacing.xs))
        }

        Column(
            modifier = Modifier.widthIn(max = maxBubbleWidth),
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
        ) {
            // Bubble body
            Box(
                modifier = Modifier
                    .clip(bubbleShape)
                    .then(
                        if (bubbleBrush != null) Modifier.background(bubbleBrush)
                        else Modifier.background(bubbleBg)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                // Text + inline time trick: invisible spacer at end keeps time from overlapping
                Column {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
                        color = textColor,
                    )
                    // Inline meta row — right-aligned inside bubble
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text(
                            text = formatTime(message.timestamp),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = metaColor,
                        )
                        if (isMine) MessageStatusIcon(message.status)
                    }
                }
            }
        }

        if (isMine) Spacer(Modifier.width(Spacing.xs))
    }
}

// ── Status icon ───────────────────────────────────────────────────────────────

@Composable
private fun MessageStatusIcon(status: MessageStatus) {
    val tint by animateColorAsState(
        targetValue = when (status) {
            MessageStatus.SENDING   -> Color.White.copy(alpha = 0.4f)
            MessageStatus.SENT      -> Color.White.copy(alpha = 0.6f)
            MessageStatus.DELIVERED -> Color.White.copy(alpha = 0.85f)
            MessageStatus.READ      -> Color(0xFF93C5FD)
        },
        label = "statusTint",
    )
    Icon(
        imageVector = if (status == MessageStatus.SENDING) Icons.Filled.Schedule else Icons.Filled.DoneAll,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(13.dp),
    )
}

// ── Typing bubble ─────────────────────────────────────────────────────────────

@Composable
private fun TypingBubble() {
    val transition = rememberInfiniteTransition(label = "typing")
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        SynoraAvatar(displayName = "?", size = 26.dp)
        Spacer(Modifier.width(Spacing.xs))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 13.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listOf(0, 160, 320).forEach { delay ->
                    val y by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = -5f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, delayMillis = delay, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse,
                        ),
                        label = "dot$delay",
                    )
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .offset(y = y.dp)
                            .clip(CircleShape)
                            .background(Indigo500.copy(alpha = 0.55f)),
                    )
                }
            }
        }
    }
}

// ── Date header ───────────────────────────────────────────────────────────────

@Composable
private fun DateHeader(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
                .padding(horizontal = 14.dp, vertical = 4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ── Input bar ─────────────────────────────────────────────────────────────────

@Composable
private fun InputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    val canSend = text.isNotBlank()

    val sendBg by animateColorAsState(
        targetValue = if (canSend) Indigo500 else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(200),
        label = "sendBg",
    )
    val sendIconTint by animateColorAsState(
        targetValue = if (canSend) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(200),
        label = "sendTint",
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xs, vertical = Spacing.xs),
            verticalAlignment = Alignment.Bottom,
        ) {
            // Emoji
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterVertically),
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEmotions,
                    contentDescription = "Emoji",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp),
                )
            }

            // Text input
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        "Message",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp)),
                maxLines = 6,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor  = Color.Transparent,
                    focusedTextColor        = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
                ),
            )

            Spacer(Modifier.width(Spacing.xs))

            // Send / Attach button
            AnimatedContent(
                targetState = canSend,
                transitionSpec = {
                    (scaleIn(tween(160)) + fadeIn(tween(160))) togetherWith
                    (scaleOut(tween(160)) + fadeOut(tween(160)))
                },
                label = "sendToggle",
                modifier = Modifier.align(Alignment.CenterVertically),
            ) { sending ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(sendBg),
                ) {
                    IconButton(onClick = { if (sending) onSend() }) {
                        Icon(
                            imageVector = if (sending) Icons.AutoMirrored.Filled.Send
                                          else Icons.Filled.AttachFile,
                            contentDescription = if (sending) "Send" else "Attach",
                            tint = sendIconTint,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer { rotationZ = if (sending) -30f else 0f },
                        )
                    }
                }
            }
        }
    }
}

// ── Formatters ────────────────────────────────────────────────────────────────

private fun formatTime(ts: Long): String =
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(ts))

private fun formatDateHeader(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    return when {
        diff < 86_400_000L  -> "Today"
        diff < 172_800_000L -> "Yesterday"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(ts))
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

private val previewMessages = listOf(
    ChatMessage(text = "Hey! Are you free for a call later? 😊",                      isMine = false, status = MessageStatus.READ),
    ChatMessage(text = "Yeah, around 3pm works for me!",                              isMine = true,  status = MessageStatus.READ),
    ChatMessage(text = "Perfect, I'll send you the link 🔗",                          isMine = false, status = MessageStatus.READ),
    ChatMessage(text = "Sounds good, see you then! Looking forward to it 🚀",         isMine = true,  status = MessageStatus.DELIVERED),
    ChatMessage(text = "Also, can you share the design files before the call?",       isMine = false, status = MessageStatus.READ),
    ChatMessage(text = "Sure, sending them now 📎",                                   isMine = true,  status = MessageStatus.SENDING),
)

@Preview(showBackground = true, name = "Chat — Light", showSystemUi = true)
@Composable
private fun ChatLightPreview() {
    SynoraTheme(darkTheme = false) {
        ChatScreenContent(
            uiState = ChatUiState(
                contactName = "Alice Johnson",
                messages = previewMessages,
                inputText = "On my way!",
            ),
            onNavigateUp = {},
            onInputChange = {},
            onSend = {},
        )
    }
}

@Preview(showBackground = true, name = "Chat — Dark", showSystemUi = true)
@Composable
private fun ChatDarkPreview() {
    SynoraTheme(darkTheme = true) {
        ChatScreenContent(
            uiState = ChatUiState(
                contactName = "Alice Johnson",
                messages = previewMessages,
                isContactTyping = true,
            ),
            onNavigateUp = {},
            onInputChange = {},
            onSend = {},
        )
    }
}
