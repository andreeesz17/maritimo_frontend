package com.maritimo.control.ui.tutor

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class ChatMessage(val text: String, val isFromUser: Boolean)

@HiltViewModel
class IATutorViewModel @Inject constructor() : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> get() = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        _messages.add(ChatMessage("¡Hola! Soy tu asistente de control portuario. ¿En qué puedo ayudarte hoy?", false))
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        _messages.add(ChatMessage(text, true))
        _messages.add(ChatMessage("Simulador de asistencia: recibí tu consulta sobre: '$text'", false))
    }
}
