package com.maritimo.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.maritimo.control.domain.repository.AtraqueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class OrdersUiState(
    val isLoading: Boolean = false,
    val orders: List<Any> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val atraqueRepository: AtraqueRepository
) : ViewModel() {
    val uiState = MutableStateFlow(OrdersUiState())
}