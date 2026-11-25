package com.example.recordatoriocontextuales.data

import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.EstadoRecordatorio
import com.example.recordatoriocontextuales.model.Prioridad
import com.example.recordatoriocontextuales.model.Recordatorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryRecordatorioRepository(
    iniciales: List<Recordatorio> = ReminderData.recordatorios,
    demoraMs: Long = 500L
) : RecordatorioRepository {
    private val demora = demoraMs.also { require(it >= 0) }
    private val mutex = Mutex()
    private val recordatorios = iniciales.toMutableList()
    private var ultimoId = iniciales.maxOfOrNull { it.id } ?: 0

    init {
        require(iniciales.map { it.id }.distinct().size == iniciales.size)
    }

    override suspend fun obtenerTodos(): List<Recordatorio> {
        delay(demora)
        return mutex.withLock { recordatorios.toList() }
    }

    override suspend fun obtenerPorId(id: Int): Recordatorio? {
        delay(demora)
        return mutex.withLock { recordatorios.firstOrNull { it.id == id } }
    }

    override suspend fun crear(
        titulo: String,
        descripcion: String,
        categoria: Categoria,
        prioridad: Prioridad
    ): Recordatorio {
        val tituloLimpio = titulo.trim()
        require(tituloLimpio.length >= 4) {
            "El título debe tener al menos 4 caracteres"
        }
        require(descripcion.length <= 200) {
            "La descripción no puede superar los 200 caracteres"
        }
        delay(demora)
        return mutex.withLock {
            check(ultimoId < Int.MAX_VALUE) { "No hay identificadores disponibles" }
            val nuevo = Recordatorio(
                id = ultimoId + 1,
                titulo = tituloLimpio,
                descripcion = descripcion,
                categoria = categoria,
                prioridad = prioridad,
                estado = EstadoRecordatorio.Pendiente
            )
            recordatorios.add(nuevo)
            ultimoId = nuevo.id
            nuevo
        }
    }
}
