package com.maritimo.control.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.MuelleDto
import com.maritimo.control.data.remote.dto.MuelleCreateDto
import com.maritimo.control.data.remote.dto.PuertoDto
import com.maritimo.control.domain.repository.AtraqueRepository
import com.maritimo.control.domain.repository.MuelleRepository
import com.maritimo.control.domain.repository.PuertoRepository
import com.maritimo.control.util.HttpErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MuelleAllocationUiState(
    val isLoading: Boolean = false,
    val muelles: List<MuelleDto> = emptyList(),
    val puertos: List<PuertoDto> = emptyList(),
    val atraques: List<AtraqueDto> = emptyList(),
    val error: String? = null,
    val muelleSearch: String = "",
    val puertoSearch: String = "",
    val muellePage: Int = 1,
    val puertoPage: Int = 1,
    val hasMoreMuelles: Boolean = false,
    val hasMorePuertos: Boolean = false
)

@HiltViewModel
class MuelleAllocationViewModel @Inject constructor(
    private val muelleRepository: MuelleRepository,
    private val puertoRepository: PuertoRepository,
    private val atraqueRepository: AtraqueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MuelleAllocationUiState())
    val uiState: StateFlow<MuelleAllocationUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun updateMuelleSearch(query: String) {
        _uiState.update { it.copy(muelleSearch = query) }
        loadData()
    }

    fun updatePuertoSearch(query: String) {
        _uiState.update { it.copy(puertoSearch = query) }
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, muellePage = 1, puertoPage = 1) }
            try {
                val muellesResponse = muelleRepository.getMuelles(
                    page = 1,
                    search = _uiState.value.muelleSearch.takeIf { it.isNotBlank() }
                )
                val puertosResponse = puertoRepository.getPuertos(
                    page = 1,
                    search = _uiState.value.puertoSearch.takeIf { it.isNotBlank() }
                )
                val atraquesResponse = atraqueRepository.getAtraques()

                if (muellesResponse.isSuccessful && puertosResponse.isSuccessful && atraquesResponse.isSuccessful) {
                    val mList = muellesResponse.body()?.results ?: emptyList()
                    val pList = puertosResponse.body()?.results ?: emptyList()
                    val aList = atraquesResponse.body()?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            muelles = mList,
                            puertos = pList,
                            atraques = aList,
                            hasMoreMuelles = muellesResponse.body()?.next != null,
                            hasMorePuertos = puertosResponse.body()?.next != null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al consultar API: Muelles(${muellesResponse.code()}) Puertos(${puertosResponse.code()})"
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

    fun loadMoreMuelles() {
        if (_uiState.value.isLoading || !_uiState.value.hasMoreMuelles) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextPage = _uiState.value.muellePage + 1
            try {
                val response = muelleRepository.getMuelles(
                    page = nextPage,
                    search = _uiState.value.muelleSearch.takeIf { it.isNotBlank() }
                )
                if (response.isSuccessful) {
                    val list = response.body()?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            muelles = it.muelles + list,
                            muellePage = nextPage,
                            hasMoreMuelles = response.body()?.next != null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    fun loadMorePuertos() {
        if (_uiState.value.isLoading || !_uiState.value.hasMorePuertos) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nextPage = _uiState.value.puertoPage + 1
            try {
                val response = puertoRepository.getPuertos(
                    page = nextPage,
                    search = _uiState.value.puertoSearch.takeIf { it.isNotBlank() }
                )
                if (response.isSuccessful) {
                    val list = response.body()?.results ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            puertos = it.puertos + list,
                            puertoPage = nextPage,
                            hasMorePuertos = response.body()?.next != null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = HttpErrorHandler.parseException(e)) }
            }
        }
    }

    // CRUD Muelles
    fun createMuelle(codigo: String, capacidad: Int, estado: String, puertoId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = muelleRepository.createMuelle(MuelleCreateDto(puertoId, codigo, capacidad, estado))
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

    fun updateMuelle(id: Int, codigo: String, capacidad: Int, estado: String, puertoId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = muelleRepository.updateMuelle(id, MuelleCreateDto(puertoId, codigo, capacidad, estado))
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

    fun deleteMuelle(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = muelleRepository.deleteMuelle(id)
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

    // CRUD Puertos
    fun createPuerto(puerto: PuertoDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = puertoRepository.createPuerto(puerto)
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

    fun updatePuerto(id: Int, puerto: PuertoDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = puertoRepository.updatePuerto(id, puerto)
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

    fun deletePuerto(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = puertoRepository.deletePuerto(id)
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
