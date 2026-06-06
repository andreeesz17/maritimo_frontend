package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.MuelleDto
import com.maritimo.control.data.remote.dto.MuelleCreateDto
import com.maritimo.control.data.remote.dto.MuelleResponse
import retrofit2.Response
import retrofit2.http.*

interface MuelleApi {
    @GET("api/muelles/")
    suspend fun getMuelles(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("estado") estado: String? = null,
        @Query("puerto") puertoId: Int? = null,
        @Query("search") search: String? = null
    ): Response<MuelleResponse>

    @GET("api/muelles/{id}/")
    suspend fun getMuelleById(@Path("id") id: Int): Response<MuelleDto>

    @POST("api/muelles/")
    suspend fun createMuelle(@Body muelle: MuelleCreateDto): Response<MuelleDto>

    @PUT("api/muelles/{id}/")
    suspend fun updateMuelle(@Path("id") id: Int, @Body muelle: MuelleCreateDto): Response<MuelleDto>

    @DELETE("api/muelles/{id}/")
    suspend fun deleteMuelle(@Path("id") id: Int): Response<Unit>
}
