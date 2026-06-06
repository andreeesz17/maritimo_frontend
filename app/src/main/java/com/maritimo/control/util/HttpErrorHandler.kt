package com.maritimo.control.util

import retrofit2.Response
import java.io.IOException
import org.json.JSONObject
import org.json.JSONArray

object HttpErrorHandler {
    fun parseError(response: Response<*>): String {
        val errorBodyString = try {
            response.errorBody()?.string()
        } catch (e: Exception) {
            null
        }

        if (!errorBodyString.isNullOrBlank()) {
            try {
                val json = JSONObject(errorBodyString)
                val keys = json.keys()
                val errorMessages = mutableListOf<String>()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = json.get(key)
                    if (value is JSONArray) {
                        val messages = mutableListOf<String>()
                        for (i in 0 until value.length()) {
                            messages.add(value.getString(i))
                        }
                        errorMessages.add("$key: ${messages.joinToString(", ")}")
                    } else if (value is String) {
                        errorMessages.add("$key: $value")
                    } else {
                        errorMessages.add("$key: ${value.toString()}")
                    }
                }
                if (errorMessages.isNotEmpty()) {
                    return errorMessages.joinToString("\n")
                }
            } catch (e: Exception) {
                // Si no es un JSON válido o falla, mostramos el string directo si es corto
                if (errorBodyString.length < 100) {
                    return errorBodyString
                }
            }
        }

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
