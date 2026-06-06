package com.maritimo.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maritimo.control.domain.repository.AtraqueRepository
import com.maritimo.control.domain.repository.BuqueRepository
import com.maritimo.control.domain.repository.InspeccionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamificationViewModel @Inject constructor(
    private val buqueRepository: BuqueRepository,
    private val atraqueRepository: AtraqueRepository,
    private val inspeccionRepository: InspeccionRepository
) : ViewModel() {

    private val _buquesCount = MutableStateFlow(0)
    val buquesCount: StateFlow<Int> = _buquesCount.asStateFlow()

    private val _atraquesCount = MutableStateFlow(0)
    val atraquesCount: StateFlow<Int> = _atraquesCount.asStateFlow()

    private val _inspeccionesCount = MutableStateFlow(0)
    val inspeccionesCount: StateFlow<Int> = _inspeccionesCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRealStats()
    }

    fun loadRealStats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch buques total count
                val buquesResponse = buqueRepository.getBuques(page = 1)
                if (buquesResponse.isSuccessful) {
                    _buquesCount.value = buquesResponse.body()?.count ?: 0
                }

                // Fetch atraques total count
                val atraquesResponse = atraqueRepository.getAtraques(page = 1)
                if (atraquesResponse.isSuccessful) {
                    _atraquesCount.value = atraquesResponse.body()?.count ?: 0
                }

                // Fetch inspecciones total count
                val inspeccionesResponse = inspeccionRepository.getInspecciones(page = 1)
                if (inspeccionesResponse.isSuccessful) {
                    _inspeccionesCount.value = inspeccionesResponse.body()?.count ?: 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}