package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.PuertoDto
import com.maritimo.control.data.remote.dto.PuertoResponse
import retrofit2.Response
import retrofit2.http.*

interface PuertoApi {
    @GET("api/puertos/")
    suspend fun getPuertos(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("estado") estado: String? = null,
        @Query("ciudad") ciudad: String? = null,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null
    ): Response<PuertoResponse>

    @GET("api/puertos/{id}/")
    suspend fun getPuertoById(@Path("id") id: Int): Response<PuertoDto>

    @POST("api/puertos/")
    suspend fun createPuerto(@Body puerto: PuertoDto): Response<PuertoDto>

    @PUT("api/puertos/{id}/")
    suspend fun updatePuerto(@Path("id") id: Int, @Body puerto: PuertoDto): Response<PuertoDto>

    @PATCH("api/puertos/{id}/")
    suspend fun patchPuerto(@Path("id") id: Int, @Body fields: Map<String, @JvmSuppressWildcards Any>): Response<PuertoDto>

    @DELETE("api/puertos/{id}/")
    suspend fun deletePuerto(@Path("id") id: Int): Response<Unit>
}
