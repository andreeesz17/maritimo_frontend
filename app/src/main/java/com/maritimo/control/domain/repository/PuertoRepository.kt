package com.maritimo.control.domain.repository

import com.maritimo.control.data.remote.dto.PuertoDto
import com.maritimo.control.data.remote.dto.PuertoResponse
import retrofit2.Response

interface PuertoRepository {
    suspend fun getPuertos(
        page: Int? = null,
        pageSize: Int? = null,
        estado: String? = null,
        ciudad: String? = null,
        search: String? = null,
        ordering: String? = null
    ): Response<PuertoResponse>

    suspend fun getPuertoById(id: Int): Response<PuertoDto>
    suspend fun createPuerto(puerto: PuertoDto): Response<PuertoDto>
    suspend fun updatePuerto(id: Int, puerto: PuertoDto): Response<PuertoDto>
    suspend fun patchPuerto(id: Int, fields: Map<String, @JvmSuppressWildcards Any>): Response<PuertoDto>
    suspend fun deletePuerto(id: Int): Response<Unit>
}
