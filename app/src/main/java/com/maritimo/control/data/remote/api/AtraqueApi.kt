package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.AtraqueCreateDto
import com.maritimo.control.data.remote.dto.AtraqueResponse
import retrofit2.Response
import retrofit2.http.*

interface AtraqueApi {
    @GET("api/atraques/")
    suspend fun getAtraques(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("estado") estado: String? = null,
        @Query("muelle") muelleId: Int? = null,
        @Query("buque") buqueId: Int? = null,
        @Query("capitan") capitanId: Int? = null
    ): Response<AtraqueResponse>

    @GET("api/atraques/{id}/")
    suspend fun getAtraqueById(@Path("id") id: Int): Response<AtraqueDto>

    @POST("api/atraques/")
    suspend fun createAtraque(@Body atraque: AtraqueCreateDto): Response<AtraqueDto>

    @PUT("api/atraques/{id}/")
    suspend fun updateAtraque(@Path("id") id: Int, @Body atraque: AtraqueCreateDto): Response<AtraqueDto>

    @DELETE("api/atraques/{id}/")
    suspend fun deleteAtraque(@Path("id") id: Int): Response<Unit>
}
