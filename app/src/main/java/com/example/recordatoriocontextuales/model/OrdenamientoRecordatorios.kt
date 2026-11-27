package com.example.recordatoriocontextuales.model

fun aplicarPreferencias(registros: List<Recordatorio>, preferencias: PreferenciasUsuario): List<Recordatorio> {
    val visibles = registros.filter { preferencias.mostrarCompletados || it.estado != EstadoRecordatorio.Completado }
    val porFecha = compareBy<Recordatorio> { it.fechaVencimiento == null }.thenBy { it.fechaVencimiento }.thenBy { it.id }
    return when (preferencias.orden) {
        OrdenRecordatorios.FECHA -> visibles.sortedWith(porFecha)
        OrdenRecordatorios.PRIORIDAD -> visibles.sortedWith(compareByDescending<Recordatorio> { it.prioridad.nivel }.then(porFecha))
    }
}
