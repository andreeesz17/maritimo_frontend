package com.maritimo.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CapitanDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("licencia_navegacion") val licenciaNavegacion: String,
    @SerializedName("nacionalidad") val nacionalidad: String
)

data class CapitanResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<CapitanDto>
)
