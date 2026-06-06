package com.maritimo.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PuertoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("capacidad_maxima_buques") val capacidadMaximaBuques: Int,
    @SerializedName("estado") val estado: String
)

data class PuertoResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PuertoDto>
)
