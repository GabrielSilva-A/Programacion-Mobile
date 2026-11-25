package com.example.recordatoriocontextuales.data

import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.EstadoRecordatorio
import com.example.recordatoriocontextuales.model.Prioridad
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class RecordatorioRepositoryTest {
    private fun repositorio() = InMemoryRecordatorioRepository(demoraMs = 0)

    @Test
    fun cargaCincoRecordatoriosOriginales() = runBlocking {
        assertEquals(ReminderData.recordatorios, repositorio().obtenerTodos())
    }

    @Test
    fun buscaPorIdYDevuelveNullSiNoExiste() = runBlocking {
        val repo = repositorio()
        assertEquals(1, repo.obtenerPorId(1)?.id)
        assertNull(repo.obtenerPorId(999))
    }

    @Test
    fun creaYRecuperaRecordatorio() = runBlocking {
        val repo = repositorio()
        val nuevo = repo.crear("  Nuevo aviso  ", "Detalle", Categoria.Personal, Prioridad.Alta)
        assertEquals(6, nuevo.id)
        assertEquals("Nuevo aviso", nuevo.titulo)
        assertEquals("Detalle", nuevo.descripcion)
        assertEquals(Categoria.Personal, nuevo.categoria)
        assertEquals(Prioridad.Alta, nuevo.prioridad)
        assertEquals(EstadoRecordatorio.Pendiente, nuevo.estado)
        assertFalse(nuevo.sincronizado)
        assertEquals(nuevo, repo.obtenerPorId(6))
        assertEquals(6, repo.obtenerTodos().size)
        assertEquals(5, repositorio().obtenerTodos().size)
    }

    @Test
    fun admiteListaVacia() = runBlocking {
        val repo = InMemoryRecordatorioRepository(emptyList(), 0)
        assertTrue(repo.obtenerTodos().isEmpty())
        assertEquals(1, repo.crear("Aviso", "", Categoria.Salud, Prioridad.Baja).id)
    }

    @Test
    fun rechazaDatosInvalidosSinConsumirId() = runBlocking {
        val repo = repositorio()
        for ((titulo, descripcion) in listOf(" abc " to "", "Aviso" to "x".repeat(201))) {
            try {
                repo.crear(titulo, descripcion, Categoria.Trabajo, Prioridad.Media)
                fail("Se esperaba IllegalArgumentException")
            } catch (_: IllegalArgumentException) {
                // Validación esperada.
            }
        }
        assertEquals(5, repo.obtenerTodos().size)
        assertEquals(6, repo.crear("Aviso", "x".repeat(200), Categoria.Trabajo, Prioridad.Media).id)
    }

    @Test
    fun asignaIdsUnicosEnCreacionesConcurrentes() = runBlocking {
        val repo = InMemoryRecordatorioRepository(emptyList(), 1)
        val nuevos = (1..20).map { n ->
            async(kotlinx.coroutines.Dispatchers.Default) {
                repo.crear("Aviso $n", "", Categoria.Estudios, Prioridad.Media)
            }
        }.awaitAll()
        assertEquals((1..20).toSet(), nuevos.map { it.id }.toSet())
        assertEquals(20, repo.obtenerTodos().size)
    }

    @Test
    fun devuelveUnaCopiaDeLaLista() = runBlocking {
        val repo = repositorio()
        val anterior = repo.obtenerTodos()
        repo.crear("Aviso", "", Categoria.Personal, Prioridad.Baja)
        assertEquals(5, anterior.size)
        assertEquals(6, repo.obtenerTodos().size)
    }
}
