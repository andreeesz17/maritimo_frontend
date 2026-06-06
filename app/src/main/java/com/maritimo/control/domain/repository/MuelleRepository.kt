package com.maritimo.control.domain.repository

import com.maritimo.control.data.remote.dto.MuelleDto
import com.maritimo.control.data.remote.dto.MuelleCreateDto
import com.maritimo.control.data.remote.dto.MuelleResponse
import retrofit2.Response

interface MuelleRepository {
    suspend fun getMuelles(
        page: Int? = null,
        pageSize: Int? = null,
        estado: String? = null,
        puertoId: Int? = null,
        search: String? = null
    ): Response<MuelleResponse>

    suspend fun getMuelleById(id: Int): Response<MuelleDto>
    suspend fun createMuelle(muelle: MuelleCreateDto): Response<MuelleDto>
    suspend fun updateMuelle(id: Int, muelle: MuelleCreateDto): Response<MuelleDto>
    suspend fun deleteMuelle(id: Int): Response<Unit>
}
