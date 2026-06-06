package com.maritimo.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maritimo.control.data.remote.dto.BuqueDto
import com.maritimo.control.domain.repository.AuthRepository
import com.maritimo.control.domain.repository.BuqueRepository
import com.maritimo.control.util.HttpErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val buques: List<BuqueDto> = emptyList(),
    val userName: String = "",
    val error: String? = null,
    val hasMore: Boolean = false,
    val currentPage: Int = 1,
    val searchQuery: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val buqueRepository: BuqueRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadHomeData(search = query)
    }

    fun loadHomeData(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentPage = 1, buques = emptyList()) }
            
            val currentUser = authRepository.getStoredUser()
            val name = currentUser?.username ?: "Operador"
            _uiState.update { it.copy(userName = name) }

            val query = search ?: _uiState.value.searchQuery

            try {
                val response = buqueRepository.getBuques(page = 1, search = query.takeIf { it.isNotBlank() })
                if (response.isSuccessful) {
                    val body = response.body()
                    val buqueList = body?.results ?: emptyList()
                    val nextUrl = body?.next
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            buques = buqueList,
                            hasMore = nextUrl != null,
                            currentPage = 1
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = HttpErrorHandler.parseError(response)
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = HttpErrorHandler.parseException(e)
                    ) 
                }
            }
        }
    }

    fun loadMoreBuques() {
        if (_uiState.value.isLoading || !_uiState.value.hasMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextPage = _uiState.value.currentPage + 1
            val query = _uiState.value.searchQuery
            try {
                val response = buqueRepository.getBuques(page = nextPage, search = query.takeIf { it.isNotBlank() })
                if (response.isSuccessful) {
                    val body = response.body()
                    val buqueList = body?.results ?: emptyList()
                    val nextUrl = body?.next
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            buques = it.buques + buqueList,
                            hasMore = nextUrl != null,
                            currentPage = nextPage
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = HttpErrorHandler.parseError(response)
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = HttpErrorHandler.parseException(e)
                    ) 
                }
            }
        }
    }

    fun createBuque(buque: BuqueDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = buqueRepository.createBuque(buque)
                if (response.isSuccessful) {
                    loadHomeData()
                    onSuccess()
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = HttpErrorHandler.parseError(response)
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = HttpErrorHandler.parseException(e)
                    ) 
                }
            }
        }
    }

    fun updateBuque(id: Int, buque: BuqueDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = buqueRepository.updateBuque(id, buque)
                if (response.isSuccessful) {
                    loadHomeData()
                    onSuccess()
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = HttpErrorHandler.parseError(response)
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = HttpErrorHandler.parseException(e)
                    ) 
                }
            }
        }
    }

    fun deleteBuque(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = buqueRepository.deleteBuque(id)
                if (response.isSuccessful) {
                    loadHomeData()
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = HttpErrorHandler.parseError(response)
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = HttpErrorHandler.parseException(e)
                    ) 
                }
            }
        }
    }

    fun refreshStats() {
        loadHomeData()
    }
}