package com.example.recordatoriocontextuales.navigation

object Routes {
    const val CONFIGURACION = "configuracion"
    const val LISTA = "recordatorios"
    const val CREAR = "crear"
    const val ID = "id"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"
}
