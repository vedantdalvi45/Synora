package com.example.synora.ui.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.synora.ui.components.SynoraAvatar
import com.example.synora.ui.components.SynoraTopBar
import com.example.synora.ui.theme.Spacing
import com.example.synora.ui.theme.SynoraTheme

// ── Dummy data ────────────────────────────────────────────────────────────────

data class ChatPreview(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
)

val dummyChats = listOf(
    ChatPreview(1,  "Alice Johnson",  "Hey! Are you free for a call later?",         "9:41 AM",  unreadCount = 3, isOnline = true),
    ChatPreview(2,  "Bob Martinez",   "The design looks great, nice work 🔥",         "9:15 AM",  unreadCount = 1),
    ChatPreview(3,  "AI Assistant",   "I can help you summarise the meeting notes.",  "8:50 AM",  isOnline = true),
    ChatPreview(4,  "Carol White",    "Can you send me the updated file?",            "Yesterday"),
    ChatPreview(5,  "Dev Team",       "David: PR is ready for review",                "Yesterday", unreadCount = 5),
    ChatPreview(6,  "Emma Davis",     "Thanks for the help earlier!",                 "Mon"),
    ChatPreview(7,  "Frank Lee",      "Let's sync up tomorrow morning.",              "Mon"),
    ChatPreview(8,  "Grace Kim",      "Sent you the invoice 📎",                      "Sun"),
    ChatPreview(9,  "Henry Brown",    "Did you see the announcement?",                "Sat"),
    ChatPreview(10, "Isla Thompson",  "Sounds good, see you then!",                   "Fri"),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun ChatsScreen(onChatClick: (id: Int, name: String) -> Unit = { _, _ -> }) {
    Scaffold(
        topBar = { SynoraTopBar(title = "Chats") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* new chat — Phase 3 */ },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "New chat",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(dummyChats, key = { it.id }) { chat ->
                ChatRow(chat = chat, onClick = { onChatClick(chat.id, chat.name) })
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    thickness = 0.5.dp,
                )
            }
        }
    }
}

// ── Chat row ──────────────────────────────────────────────────────────────────

@Composable
private fun ChatRow(chat: ChatPreview, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm + 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            BadgedBox(
                badge = {
                    if (chat.unreadCount > 0) {
                        Badge(containerColor = MaterialTheme.colorScheme.primary) {
                            Text(
                                text = if (chat.unreadCount > 9) "9+" else chat.unreadCount.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            ) {
                SynoraAvatar(displayName = chat.name, size = 48.dp)
            }
            if (chat.isOnline) {
                Canvas(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.BottomEnd),
                ) {
                    drawCircle(color = Color(0xFF22C55E))
                }
            }
        }

        Spacer(Modifier.width(Spacing.md))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = chat.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = chat.lastMessage,
                style = MaterialTheme.typography.bodySmall,
                color = if (chat.unreadCount > 0)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (chat.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(Modifier.width(Spacing.sm))

        Text(
            text = chat.time,
            style = MaterialTheme.typography.labelSmall,
            color = if (chat.unreadCount > 0)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Chats — Light")
@Composable
private fun ChatsLightPreview() {
    SynoraTheme(darkTheme = false) { ChatsScreen() }
}

@Preview(showBackground = true, name = "Chats — Dark")
@Composable
private fun ChatsDarkPreview() {
    SynoraTheme(darkTheme = true) { ChatsScreen() }
}
