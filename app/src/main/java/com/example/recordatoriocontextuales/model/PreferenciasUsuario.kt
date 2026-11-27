package com.example.recordatoriocontextuales.model

enum class OrdenRecordatorios { FECHA, PRIORIDAD }

data class PreferenciasUsuario(
    val modoOscuro: Boolean = false,
    val mostrarCompletados: Boolean = true,
    val orden: OrdenRecordatorios = OrdenRecordatorios.FECHA
)
