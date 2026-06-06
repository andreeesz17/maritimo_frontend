package com.maritimo.control.util

import retrofit2.Response
import java.io.IOException

object HttpErrorHandler {
    fun parseError(response: Response<*>): String {
        return when (response.code()) {
            400 -> "Datos inválidos o incompletos."
            401, 403 -> "Usuario no autorizado o sesión expirada."
            404 -> "El registro solicitado no existe."
            500 -> "Error interno del servidor de puertos."
            else -> "Error en el servidor: ${response.code()} ${response.message()}"
        }
    }

    fun parseException(t: Throwable): String {
        return if (t is IOException) {
            "Comprueba tu conexión a internet."
        } else {
            "Ocurrió un error inesperado: ${t.localizedMessage ?: "desconocido"}"
        }
    }
}
