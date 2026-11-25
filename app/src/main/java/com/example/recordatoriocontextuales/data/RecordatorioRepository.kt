package com.example.recordatoriocontextuales.data

import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.Prioridad
import com.example.recordatoriocontextuales.model.Recordatorio

interface RecordatorioRepository {
    suspend fun obtenerTodos(): List<Recordatorio>
    suspend fun obtenerPorId(id: Int): Recordatorio?
    suspend fun crear(
        titulo: String,
        descripcion: String,
        categoria: Categoria,
        prioridad: Prioridad
    ): Recordatorio
}
