package com.example.recordatoriocontextuales.ui.viewmodel

import com.example.recordatoriocontextuales.data.InMemoryRecordatorioRepository
import com.example.recordatoriocontextuales.data.RecordatorioRepository
import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.Prioridad
import com.example.recordatoriocontextuales.model.Recordatorio
import com.example.recordatoriocontextuales.ui.state.ListaUiState
import com.example.recordatoriocontextuales.ui.state.DetalleUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RecordatoriosViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun preparar() { Dispatchers.setMain(dispatcher) }
    @After fun limpiar() { Dispatchers.resetMain() }

    @Test fun cargaInicial() = runTest {
        val vm = RecordatoriosViewModel(InMemoryRecordatorioRepository(demoraMs = 500))
        assertEquals(ListaUiState.Cargando, vm.lista.value)
        advanceUntilIdle()
        assertEquals(5, (vm.lista.value as ListaUiState.Datos).recordatorios.size)
    }

    @Test fun listaVacia() = runTest {
        val vm = RecordatoriosViewModel(InMemoryRecordatorioRepository(emptyList(), 0))
        advanceUntilIdle()
        assertEquals(ListaUiState.Vacia, vm.lista.value)
    }

    @Test fun errorYReintento() = runTest {
        val base = InMemoryRecordatorioRepository(demoraMs = 0)
        var fallar = true
        val repo = object : RecordatorioRepository by base {
            override suspend fun obtenerTodos(): List<Recordatorio> {
                if (fallar) error("Fallo simulado")
                return base.obtenerTodos()
            }
        }
        val vm = RecordatoriosViewModel(repo)
        advanceUntilIdle()
        assertTrue(vm.lista.value is ListaUiState.Error)
        fallar = false
        vm.cargarLista()
        advanceUntilIdle()
        assertTrue(vm.lista.value is ListaUiState.Datos)
    }

    private fun completar(vm: RecordatoriosViewModel) {
        vm.cambiarTitulo("  Nuevo aviso  ")
        vm.cambiarDescripcion("Detalle")
        vm.cambiarCategoria(Categoria.Personal)
        vm.cambiarPrioridad(Prioridad.Alta)
    }

    @Test fun formularioInvalido() = runTest {
        val vm = RecordatoriosViewModel(InMemoryRecordatorioRepository(demoraMs = 0))
        assertFalse(vm.creacion.value.puedeGuardar)
        vm.guardar()
        completar(vm)
        vm.cambiarDescripcion("x".repeat(201))
        assertFalse(vm.creacion.value.puedeGuardar)
        vm.guardar()
        advanceUntilIdle()
        assertNull(vm.creacion.value.idGuardado)
        assertEquals(5, (vm.lista.value as ListaUiState.Datos).recordatorios.size)
    }

    @Test fun guardarActualizaListaSinDuplicados() = runTest {
        val vm = RecordatoriosViewModel(InMemoryRecordatorioRepository(demoraMs = 500))
        advanceUntilIdle()
        completar(vm)
        assertTrue(vm.creacion.value.puedeGuardar)
        vm.guardar()
        assertTrue(vm.creacion.value.guardando)
        vm.guardar()
        advanceUntilIdle()
        assertEquals(6, vm.creacion.value.idGuardado)
        assertFalse(vm.creacion.value.guardando)
        assertEquals(6, (vm.lista.value as ListaUiState.Datos).recordatorios.size)
        vm.reiniciarCreacion()
        assertNull(vm.creacion.value.idGuardado)
    }

    @Test fun errorGuardadoPermiteReintentar() = runTest {
        val base = InMemoryRecordatorioRepository(demoraMs = 0)
        var fallar = true
        val repo = object : RecordatorioRepository by base {
            override suspend fun crear(titulo: String, descripcion: String, categoria: Categoria, prioridad: Prioridad): Recordatorio {
                if (fallar) error("Fallo simulado")
                return base.crear(titulo, descripcion, categoria, prioridad)
            }
        }
        val vm = RecordatoriosViewModel(repo)
        completar(vm)
        vm.guardar()
        advanceUntilIdle()
        assertNotNull(vm.creacion.value.errorGuardado)
        assertTrue(vm.creacion.value.puedeGuardar)
        fallar = false
        vm.guardar()
        advanceUntilIdle()
        assertEquals(6, vm.creacion.value.idGuardado)
    }

    @Test fun detalleExistenteYAusente() = runTest {
        val vm = RecordatoriosViewModel(InMemoryRecordatorioRepository(demoraMs = 0))
        vm.cargarDetalle(1)
        advanceUntilIdle()
        assertEquals(1, (vm.detalle.value as DetalleUiState.Datos).recordatorio.id)
        vm.cargarDetalle(999)
        advanceUntilIdle()
        assertEquals(DetalleUiState.NoEncontrado, vm.detalle.value)
    }
}
