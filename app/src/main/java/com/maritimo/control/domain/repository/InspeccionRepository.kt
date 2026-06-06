package com.maritimo.control.domain.repository

import com.maritimo.control.data.remote.dto.InspeccionDto
import com.maritimo.control.data.remote.dto.InspeccionCreateDto
import com.maritimo.control.data.remote.dto.InspeccionResponse
import retrofit2.Response

interface InspeccionRepository {
    suspend fun getInspecciones(
        page: Int? = null,
        pageSize: Int? = null,
        resultado: String? = null,
        atraqueId: Int? = null
    ): Response<InspeccionResponse>

    suspend fun getInspeccionById(id: Int): Response<InspeccionDto>
    suspend fun createInspeccion(inspeccion: InspeccionCreateDto): Response<InspeccionDto>
    suspend fun updateInspeccion(id: Int, inspeccion: InspeccionCreateDto): Response<InspeccionDto>
    suspend fun deleteInspeccion(id: Int): Response<Unit>
}
