package com.maritimo.control.data.remote.api

import com.maritimo.control.data.remote.dto.InspeccionDto
import com.maritimo.control.data.remote.dto.InspeccionCreateDto
import com.maritimo.control.data.remote.dto.InspeccionResponse
import retrofit2.Response
import retrofit2.http.*

interface InspeccionApi {
    @GET("api/inspecciones/")
    suspend fun getInspecciones(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("resultado") resultado: String? = null,
        @Query("atraque") atraqueId: Int? = null
    ): Response<InspeccionResponse>

    @GET("api/inspecciones/{id}/")
    suspend fun getInspeccionById(@Path("id") id: Int): Response<InspeccionDto>

    @POST("api/inspecciones/")
    suspend fun createInspeccion(@Body inspeccion: InspeccionCreateDto): Response<InspeccionDto>

    @PUT("api/inspecciones/{id}/")
    suspend fun updateInspeccion(@Path("id") id: Int, @Body inspeccion: InspeccionCreateDto): Response<InspeccionDto>

    @DELETE("api/inspecciones/{id}/")
    suspend fun deleteInspeccion(@Path("id") id: Int): Response<Unit>
}
