package com.maritimo.control.domain.repository

import com.maritimo.control.data.remote.dto.CapitanDto
import com.maritimo.control.data.remote.dto.CapitanResponse
import retrofit2.Response

interface CapitanRepository {
    suspend fun getCapitanes(
        page: Int? = null,
        pageSize: Int? = null,
        search: String? = null
    ): Response<CapitanResponse>

    suspend fun getCapitanById(id: Int): Response<CapitanDto>
    suspend fun createCapitan(capitan: CapitanDto): Response<CapitanDto>
    suspend fun updateCapitan(id: Int, capitan: CapitanDto): Response<CapitanDto>
    suspend fun deleteCapitan(id: Int): Response<Unit>
}
