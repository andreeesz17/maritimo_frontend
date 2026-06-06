package com.maritimo.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class CartUiState(
    val isLoading: Boolean = false,
    val items: List<Any> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {
    val uiState = MutableStateFlow(CartUiState())
    fun addToCart(course: Any) {}
    fun removeFromCart(courseId: Int) {}
    fun clearCart() {}
    fun checkout(onSuccess: (Int) -> Unit) {}
}