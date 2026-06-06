package com.maritimo.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InspeccionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("atraque") val atraque: AtraqueShortDto,
    @SerializedName("fecha_inspeccion") val fechaInspeccion: String,
    @SerializedName("resultado") val resultado: String,
    @SerializedName("observaciones") val observaciones: String
)

data class AtraqueShortDto(
    @SerializedName("id") val id: Int,
    @SerializedName("buque") val buque: BuqueNameDto,
    @SerializedName("muelle") val muelle: MuelleCodeDto
)

data class BuqueNameDto(
    @SerializedName("nombre") val nombre: String
)

data class MuelleCodeDto(
    @SerializedName("codigo") val codigo: String
)

data class InspeccionCreateDto(
    @SerializedName("atraque_id") val atraqueId: Int,
    @SerializedName("fecha_inspeccion") val fechaInspeccion: String,
    @SerializedName("resultado") val resultado: String,
    @SerializedName("observaciones") val observaciones: String
)

data class InspeccionResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<InspeccionDto>
)
