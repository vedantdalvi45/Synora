package com.example.synora.domain.model

import java.util.UUID

enum class MessageStatus { SENDING, SENT, DELIVERED, READ }

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isMine: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT,
)
