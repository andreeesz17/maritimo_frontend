package com.maritimo.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MuelleDto(
    @SerializedName("id") val id: Int,
    @SerializedName("puerto") val puerto: PuertoDto,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("capacidad_atraque") val capacidadAtraque: Int,
    @SerializedName("estado") val estado: String
)

data class MuelleCreateDto(
    @SerializedName("puerto_id") val puertoId: Int,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("capacidad_atraque") val capacidadAtraque: Int,
    @SerializedName("estado") val estado: String
)

data class MuelleResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<MuelleDto>
)
