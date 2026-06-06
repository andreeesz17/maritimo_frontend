package com.maritimo.control.data.repository

import com.maritimo.control.data.remote.api.InspeccionApi
import com.maritimo.control.data.remote.dto.InspeccionDto
import com.maritimo.control.data.remote.dto.InspeccionCreateDto
import com.maritimo.control.data.remote.dto.InspeccionResponse
import com.maritimo.control.domain.repository.InspeccionRepository
import retrofit2.Response
import javax.inject.Inject

class InspeccionRepositoryImpl @Inject constructor(
    private val api: InspeccionApi
) : InspeccionRepository {
    override suspend fun getInspecciones(
        page: Int?,
        pageSize: Int?,
        resultado: String?,
        atraqueId: Int?
    ): Response<InspeccionResponse> = api.getInspecciones(page, pageSize, resultado, atraqueId)

    override suspend fun getInspeccionById(id: Int): Response<InspeccionDto> = api.getInspeccionById(id)
    override suspend fun createInspeccion(inspeccion: InspeccionCreateDto): Response<InspeccionDto> = api.createInspeccion(inspeccion)
    override suspend fun updateInspeccion(id: Int, inspeccion: InspeccionCreateDto): Response<InspeccionDto> = api.updateInspeccion(id, inspeccion)
    override suspend fun deleteInspeccion(id: Int): Response<Unit> = api.deleteInspeccion(id)
}
