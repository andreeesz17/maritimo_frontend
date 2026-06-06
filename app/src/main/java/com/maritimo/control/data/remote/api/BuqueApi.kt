package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.BuqueDto
import com.maritimo.control.data.remote.dto.BuqueResponse
import retrofit2.Response
import retrofit2.http.*

interface BuqueApi {
    @GET("api/buques/")
    suspend fun getBuques(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("tipo_buque") tipoBuque: String? = null,
        @Query("pais_origen") paisOrigen: String? = null,
        @Query("search") search: String? = null
    ): Response<BuqueResponse>

    @GET("api/buques/{id}/")
    suspend fun getBuqueById(@Path("id") id: Int): Response<BuqueDto>

    @POST("api/buques/")
    suspend fun createBuque(@Body buque: BuqueDto): Response<BuqueDto>

    @PUT("api/buques/{id}/")
    suspend fun updateBuque(@Path("id") id: Int, @Body buque: BuqueDto): Response<BuqueDto>

    @DELETE("api/buques/{id}/")
    suspend fun deleteBuque(@Path("id") id: Int): Response<Unit>
}
