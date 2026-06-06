package com.maritimo.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AtraqueDto(
    @SerializedName("id") val id: Int,
    @SerializedName("buque") val buque: BuqueShortDto,
    @SerializedName("muelle") val muelle: MuelleShortDto,
    @SerializedName("capitan") val capitan: CapitanShortDto,
    @SerializedName("fecha_ingreso") val fechaIngreso: String,
    @SerializedName("fecha_salida") val fechaSalida: String,
    @SerializedName("estado") val estado: String
)

data class BuqueShortDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("matricula") val matricula: String
)

data class PuertoShortDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

data class MuelleShortDto(
    @SerializedName("id") val id: Int,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("puerto") val puerto: PuertoShortDto
)

data class CapitanShortDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String
)

data class AtraqueCreateDto(
    @SerializedName("buque_id") val buqueId: Int,
    @SerializedName("muelle_id") val muelleId: Int,
    @SerializedName("capitan_id") val capitanId: Int,
    @SerializedName("fecha_ingreso") val fechaIngreso: String,
    @SerializedName("fecha_salida") val fechaSalida: String,
    @SerializedName("estado") val estado: String
)

data class AtraqueResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<AtraqueDto>
)
