package com.maritimo.control.ui.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.AtraqueCreateDto
import com.maritimo.control.data.remote.dto.InspeccionDto
import com.maritimo.control.data.remote.dto.InspeccionCreateDto
import com.maritimo.control.domain.repository.AtraqueRepository
import com.maritimo.control.domain.repository.InspeccionRepository
import com.maritimo.control.util.HttpErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmergencySimulationUiState(
    val isLoading: Boolean = false,
    val inspecciones: List<InspeccionDto> = emptyList(),
    val atraques: List<AtraqueDto> = emptyList(),
    val error: String? = null,
    val inspeccionSearch: String = "",
    val atraqueSearch: String = "",
    val inspeccionPage: Int = 1,
    val atraquePage: Int = 1,
    val hasMoreInspecciones: Boolean = false,
    val hasMoreAtraques: Boolean = false
)

@HiltViewModel
class EmergencySimulationViewModel @Inject constructor(
    private val inspeccionRepository: InspeccionRepository,
    private val atraqueRepository: AtraqueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmergencySimulationUiState())
    val uiState: StateFlow<EmergencySimulationUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun updateInspeccionSearch(query: String) {
        _uiState.update { it.copy(inspeccionSearch = query) }
        loadData()
    }

    fun updateAtraqueSearch(query: String) {
        _uiState.update { it.copy(atraqueSearch = query) }
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, inspeccionPage = 1, atraquePage = 1) }
            try {
                val insResponse = inspeccionRepository.getInspecciones(
                    page = 1,
                    resultado = _uiState.value.inspeccionSearch.takeIf { it.isNotBlank() }
                )
                val atrResponse = atraqueRepository.getAtraques(
                    page = 1,
                    estado = _uiState.value.atraqueSearch.takeIf { it.isNotBlank() }
                )

                if (insResponse.isSuccessful && atrResponse.isSuccessful) {
                    val insList = insResponse.body()?.results ?: emptyList()
                    val atrList = atrResponse.body()?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            inspecciones = insList,
                            atraques = atrList,
                            hasMoreInspecciones = insResponse.body()?.next != null,
                            hasMoreAtraques = atrResponse.body()?.next != null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error: Inspecciones(${insResponse.code()}) Atraques(${atrResponse.code()})"
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

    fun loadMoreInspecciones() {
        if (_uiState.value.isLoading || !_uiState.value.hasMoreInspecciones) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextPage = _uiState.value.inspeccionPage + 1
            try {
                val response = inspeccionRepository.getInspecciones(
                    page = nextPage,
                    resultado = _uiState.value.inspeccionSearch.takeIf { it.isNotBlank() }
                )
                if (response.isSuccessful) {
                    val list = response.body()?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            inspecciones = it.inspecciones + list,
                            inspeccionPage = nextPage,
                            hasMoreInspecciones = response.body()?.next != null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun loadMoreAtraques() {
        if (_uiState.value.isLoading || !_uiState.value.hasMoreAtraques) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextPage = _uiState.value.atraquePage + 1
            try {
                val response = atraqueRepository.getAtraques(
                    page = nextPage,
                    estado = _uiState.value.atraqueSearch.takeIf { it.isNotBlank() }
                )
                if (response.isSuccessful) {
                    val list = response.body()?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            atraques = it.atraques + list,
                            atraquePage = nextPage,
                            hasMoreAtraques = response.body()?.next != null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    // CRUD Inspecciones
    fun createInspeccion(atraqueId: Int, fecha: String, resultado: String, observaciones: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = inspeccionRepository.createInspeccion(
                    InspeccionCreateDto(atraqueId, fecha, resultado, observaciones)
                )
                if (response.isSuccessful) {
                    loadData()
                    onSuccess()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun updateInspeccion(id: Int, atraqueId: Int, fecha: String, resultado: String, observaciones: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = inspeccionRepository.updateInspeccion(
                    id, InspeccionCreateDto(atraqueId, fecha, resultado, observaciones)
                )
                if (response.isSuccessful) {
                    loadData()
                    onSuccess()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun deleteInspeccion(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = inspeccionRepository.deleteInspeccion(id)
                if (response.isSuccessful) {
                    loadData()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    // CRUD Atraques
    fun createAtraque(buqueId: Int, muelleId: Int, capitanId: Int, fechaIng: String, fechaSal: String, estado: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = atraqueRepository.createAtraque(
                    AtraqueCreateDto(buqueId, muelleId, capitanId, fechaIng, fechaSal, estado)
                )
                if (response.isSuccessful) {
                    loadData()
                    onSuccess()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun updateAtraque(id: Int, buqueId: Int, muelleId: Int, capitanId: Int, fechaIng: String, fechaSal: String, estado: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = atraqueRepository.updateAtraque(
                    id, AtraqueCreateDto(buqueId, muelleId, capitanId, fechaIng, fechaSal, estado)
                )
                if (response.isSuccessful) {
                    loadData()
                    onSuccess()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun deleteAtraque(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = atraqueRepository.deleteAtraque(id)
                if (response.isSuccessful) {
                    loadData()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseError(response)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }
}
