package com.maritimo.control.data.repository

import com.maritimo.control.data.remote.api.BuqueApi
import com.maritimo.control.data.remote.dto.BuqueDto
import com.maritimo.control.data.remote.dto.BuqueResponse
import com.maritimo.control.domain.repository.BuqueRepository
import retrofit2.Response
import javax.inject.Inject

class BuqueRepositoryImpl @Inject constructor(
    private val api: BuqueApi
) : BuqueRepository {
    override suspend fun getBuques(
        page: Int?,
        pageSize: Int?,
        tipoBuque: String?,
        paisOrigen: String?,
        search: String?
    ): Response<BuqueResponse> = api.getBuques(page, pageSize, tipoBuque, paisOrigen, search)

    override suspend fun getBuqueById(id: Int): Response<BuqueDto> = api.getBuqueById(id)
    override suspend fun createBuque(buque: BuqueDto): Response<BuqueDto> = api.createBuque(buque)
    override suspend fun updateBuque(id: Int, buque: BuqueDto): Response<BuqueDto> = api.updateBuque(id, buque)
    override suspend fun deleteBuque(id: Int): Response<Unit> = api.deleteBuque(id)
}
