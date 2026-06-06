package com.maritimo.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.maritimo.control.domain.repository.BuqueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class CourseDetailUiState(
    val isLoading: Boolean = false,
    val detail: Any? = null,
    val error: String? = null
)

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val buqueRepository: BuqueRepository
) : ViewModel() {
    val uiState = MutableStateFlow(CourseDetailUiState())
    fun loadCourseDetail(courseId: Int) {}
}