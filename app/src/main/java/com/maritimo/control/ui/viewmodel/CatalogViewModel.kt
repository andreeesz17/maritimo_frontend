package com.maritimo.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maritimo.control.data.remote.dto.CapitanDto
import com.maritimo.control.domain.repository.CapitanRepository
import com.maritimo.control.util.HttpErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogUiState(
    val isLoading: Boolean = false,
    val capitanes: List<CapitanDto> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val hasMore: Boolean = false
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val capitanRepository: CapitanRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadCatalog(search = query)
    }

    fun loadCatalog(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentPage = 1, capitanes = emptyList()) }
            val query = search ?: _uiState.value.searchQuery
            try {
                val response = capitanRepository.getCapitanes(page = 1, search = query.takeIf { it.isNotBlank() })
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = body?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            capitanes = list,
                            hasMore = body?.next != null,
                            currentPage = 1
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun loadMoreCapitanes() {
        if (_uiState.value.isLoading || !_uiState.value.hasMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextPage = _uiState.value.currentPage + 1
            val query = _uiState.value.searchQuery
            try {
                val response = capitanRepository.getCapitanes(page = nextPage, search = query.takeIf { it.isNotBlank() })
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = body?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            capitanes = it.capitanes + list,
                            currentPage = nextPage,
                            hasMore = body?.next != null
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun createCapitan(capitan: CapitanDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = capitanRepository.createCapitan(capitan)
                if (response.isSuccessful) {
                    loadCatalog()
                    onSuccess()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun updateCapitan(id: Int, capitan: CapitanDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = capitanRepository.updateCapitan(id, capitan)
                if (response.isSuccessful) {
                    loadCatalog()
                    onSuccess()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun deleteCapitan(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = capitanRepository.deleteCapitan(id)
                if (response.isSuccessful) {
                    loadCatalog()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }
}