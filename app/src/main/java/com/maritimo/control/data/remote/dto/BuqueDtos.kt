package com.maritimo.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BuqueDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("tipo_buque") val tipoBuque: String,
    @SerializedName("capacidad_carga") val capacidadCarga: String,
    @SerializedName("pais_origen") val paisOrigen: String
)

data class BuqueResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<BuqueDto>
)
