package com.maritimo.control.data.repository

import com.maritimo.control.data.remote.api.CapitanApi
import com.maritimo.control.data.remote.dto.CapitanDto
import com.maritimo.control.data.remote.dto.CapitanResponse
import com.maritimo.control.domain.repository.CapitanRepository
import retrofit2.Response
import javax.inject.Inject

class CapitanRepositoryImpl @Inject constructor(
    private val api: CapitanApi
) : CapitanRepository {
    override suspend fun getCapitanes(
        page: Int?,
        pageSize: Int?,
        search: String?
    ): Response<CapitanResponse> = api.getCapitanes(page, pageSize, search)

    override suspend fun getCapitanById(id: Int): Response<CapitanDto> = api.getCapitanById(id)
    override suspend fun createCapitan(capitan: CapitanDto): Response<CapitanDto> = api.createCapitan(capitan)
    override suspend fun updateCapitan(id: Int, capitan: CapitanDto): Response<CapitanDto> = api.updateCapitan(id, capitan)
    override suspend fun deleteCapitan(id: Int): Response<Unit> = api.deleteCapitan(id)
}
