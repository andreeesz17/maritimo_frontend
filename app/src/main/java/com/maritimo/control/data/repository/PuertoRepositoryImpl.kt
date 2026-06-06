package com.maritimo.control.data.repository

import com.maritimo.control.data.remote.api.PuertoApi
import com.maritimo.control.data.remote.dto.PuertoDto
import com.maritimo.control.data.remote.dto.PuertoResponse
import com.maritimo.control.domain.repository.PuertoRepository
import retrofit2.Response
import javax.inject.Inject

class PuertoRepositoryImpl @Inject constructor(
    private val api: PuertoApi
) : PuertoRepository {
    override suspend fun getPuertos(
        page: Int?,
        pageSize: Int?,
        estado: String?,
        ciudad: String?,
        search: String?,
        ordering: String?
    ): Response<PuertoResponse> = api.getPuertos(page, pageSize, estado, ciudad, search, ordering)

    override suspend fun getPuertoById(id: Int): Response<PuertoDto> = api.getPuertoById(id)
    override suspend fun createPuerto(puerto: PuertoDto): Response<PuertoDto> = api.createPuerto(puerto)
    override suspend fun updatePuerto(id: Int, puerto: PuertoDto): Response<PuertoDto> = api.updatePuerto(id, puerto)
    override suspend fun patchPuerto(id: Int, fields: Map<String, @JvmSuppressWildcards Any>): Response<PuertoDto> = api.patchPuerto(id, fields)
    override suspend fun deletePuerto(id: Int): Response<Unit> = api.deletePuerto(id)
}
