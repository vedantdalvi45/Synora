package com.example.synora.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synora.domain.model.ChatMessage
import com.example.synora.domain.model.MessageStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

data class ChatUiState(
    val contactName: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isContactTyping: Boolean = false,
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val contactId: Int = checkNotNull(savedStateHandle["contactId"])
    private val rawName: String = checkNotNull(savedStateHandle["contactName"])
    private val contactName = URLDecoder.decode(rawName, "UTF-8")

    private val _uiState = MutableStateFlow(ChatUiState(contactName = contactName))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadDummyHistory()
    }

    private fun loadDummyHistory() {
        val history = dummyHistoryFor(contactId, contactName)
        _uiState.update { it.copy(messages = history) }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        val outgoing = ChatMessage(text = text, isMine = true, status = MessageStatus.SENDING)
        _uiState.update { it.copy(messages = it.messages + outgoing, inputText = "") }

        // Simulate delivery confirmation
        viewModelScope.launch {
            delay(600)
            _uiState.update { state ->
                state.copy(messages = state.messages.map {
                    if (it.id == outgoing.id) it.copy(status = MessageStatus.DELIVERED) else it
                })
            }
            // Simulate contact typing + reply
            delay(800)
            _uiState.update { it.copy(isContactTyping = true) }
            delay(1_500)
            val reply = ChatMessage(
                text = dummyReply(text),
                isMine = false,
                status = MessageStatus.READ,
            )
            _uiState.update { it.copy(isContactTyping = false, messages = it.messages + reply) }
        }
    }
}

// ── Dummy data helpers ────────────────────────────────────────────────────────

private fun dummyHistoryFor(contactId: Int, name: String): List<ChatMessage> {
    val now = System.currentTimeMillis()
    val min = 60_000L
    return when (contactId) {
        1 -> listOf(
            ChatMessage(text = "Hey! How's it going?",                    isMine = false, timestamp = now - 60 * min),
            ChatMessage(text = "Pretty good, just finishing up some work.", isMine = true,  timestamp = now - 58 * min),
            ChatMessage(text = "Nice! Are you free for a call later?",    isMine = false, timestamp = now - 5 * min),
        )
        2 -> listOf(
            ChatMessage(text = "Just pushed the new design to Figma.",    isMine = true,  timestamp = now - 30 * min),
            ChatMessage(text = "The design looks great, nice work 🔥",    isMine = false, timestamp = now - 28 * min),
        )
        3 -> listOf(
            ChatMessage(text = "Can you summarise yesterday's meeting?",  isMine = true,  timestamp = now - 10 * min),
            ChatMessage(text = "I can help you summarise the meeting notes. Please share them with me.", isMine = false, timestamp = now - 9 * min),
        )
        else -> listOf(
            ChatMessage(text = "Hey $name!",                              isMine = true,  timestamp = now - 20 * min),
            ChatMessage(text = "Hi there! 👋",                            isMine = false, timestamp = now - 18 * min),
        )
    }
}

private val autoReplies = listOf(
    "Got it! 👍",
    "Sure, sounds good!",
    "Let me check and get back to you.",
    "Absolutely!",
    "Interesting, tell me more.",
    "On it! 🚀",
    "Thanks for letting me know.",
    "Makes sense!",
)

private fun dummyReply(incoming: String): String = autoReplies.random()
