package com.maritimo.control.domain.repository

import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.AtraqueCreateDto
import com.maritimo.control.data.remote.dto.AtraqueResponse
import retrofit2.Response

interface AtraqueRepository {
    suspend fun getAtraques(
        page: Int? = null,
        pageSize: Int? = null,
        estado: String? = null,
        muelleId: Int? = null,
        buqueId: Int? = null,
        capitanId: Int? = null
    ): Response<AtraqueResponse>

    suspend fun getAtraqueById(id: Int): Response<AtraqueDto>
    suspend fun createAtraque(atraque: AtraqueCreateDto): Response<AtraqueDto>
    suspend fun updateAtraque(id: Int, atraque: AtraqueCreateDto): Response<AtraqueDto>
    suspend fun deleteAtraque(id: Int): Response<Unit>
}
