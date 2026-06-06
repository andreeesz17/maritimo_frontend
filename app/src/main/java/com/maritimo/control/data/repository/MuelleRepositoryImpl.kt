package com.maritimo.control.data.repository

import com.maritimo.control.data.remote.api.MuelleApi
import com.maritimo.control.data.remote.dto.MuelleDto
import com.maritimo.control.data.remote.dto.MuelleCreateDto
import com.maritimo.control.data.remote.dto.MuelleResponse
import com.maritimo.control.domain.repository.MuelleRepository
import retrofit2.Response
import javax.inject.Inject

class MuelleRepositoryImpl @Inject constructor(
    private val api: MuelleApi
) : MuelleRepository {
    override suspend fun getMuelles(
        page: Int?,
        pageSize: Int?,
        estado: String?,
        puertoId: Int?,
        search: String?
    ): Response<MuelleResponse> = api.getMuelles(page, pageSize, estado, puertoId, search)

    override suspend fun getMuelleById(id: Int): Response<MuelleDto> = api.getMuelleById(id)
    override suspend fun createMuelle(muelle: MuelleCreateDto): Response<MuelleDto> = api.createMuelle(muelle)
    override suspend fun updateMuelle(id: Int, muelle: MuelleCreateDto): Response<MuelleDto> = api.updateMuelle(id, muelle)
    override suspend fun deleteMuelle(id: Int): Response<Unit> = api.deleteMuelle(id)
}
