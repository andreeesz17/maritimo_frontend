package com.maritimo.control.data.repository

import com.maritimo.control.data.remote.api.AtraqueApi
import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.AtraqueCreateDto
import com.maritimo.control.data.remote.dto.AtraqueResponse
import com.maritimo.control.domain.repository.AtraqueRepository
import retrofit2.Response
import javax.inject.Inject

class AtraqueRepositoryImpl @Inject constructor(
    private val api: AtraqueApi
) : AtraqueRepository {
    override suspend fun getAtraques(
        page: Int?,
        pageSize: Int?,
        estado: String?,
        muelleId: Int?,
        buqueId: Int?,
        capitanId: Int?
    ): Response<AtraqueResponse> = api.getAtraques(page, pageSize, estado, muelleId, buqueId, capitanId)

    override suspend fun getAtraqueById(id: Int): Response<AtraqueDto> = api.getAtraqueById(id)
    override suspend fun createAtraque(atraque: AtraqueCreateDto): Response<AtraqueDto> = api.createAtraque(atraque)
    override suspend fun updateAtraque(id: Int, atraque: AtraqueCreateDto): Response<AtraqueDto> = api.updateAtraque(id, atraque)
    override suspend fun deleteAtraque(id: Int): Response<Unit> = api.deleteAtraque(id)
}
