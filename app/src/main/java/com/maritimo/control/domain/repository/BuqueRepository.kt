package com.maritimo.control.domain.repository

import com.maritimo.control.data.remote.dto.BuqueDto
import com.maritimo.control.data.remote.dto.BuqueResponse
import retrofit2.Response

interface BuqueRepository {
    suspend fun getBuques(
        page: Int? = null,
        pageSize: Int? = null,
        tipoBuque: String? = null,
        paisOrigen: String? = null,
        search: String? = null
    ): Response<BuqueResponse>

    suspend fun getBuqueById(id: Int): Response<BuqueDto>
    suspend fun createBuque(buque: BuqueDto): Response<BuqueDto>
    suspend fun updateBuque(id: Int, buque: BuqueDto): Response<BuqueDto>
    suspend fun deleteBuque(id: Int): Response<Unit>
}
