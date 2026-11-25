package com.example.recordatoriocontextuales.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordatoriocontextuales.data.InMemoryRecordatorioRepository
import com.example.recordatoriocontextuales.data.RecordatorioRepository
import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.Prioridad
import com.example.recordatoriocontextuales.ui.state.CreacionUiState
import com.example.recordatoriocontextuales.ui.state.DetalleUiState
import com.example.recordatoriocontextuales.ui.state.ListaUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordatoriosViewModel(
    private val repositorio: RecordatorioRepository = InMemoryRecordatorioRepository()
) : ViewModel() {
    private val _lista = MutableStateFlow<ListaUiState>(ListaUiState.Cargando)
    val lista = _lista.asStateFlow()
    private val _creacion = MutableStateFlow(CreacionUiState())
    val creacion = _creacion.asStateFlow()
    private val _detalle = MutableStateFlow<DetalleUiState>(DetalleUiState.Inicial)
    val detalle = _detalle.asStateFlow()
    private var cargaLista: Job? = null
    private var cargaDetalle: Job? = null

    init { cargarLista() }

    fun cargarLista() {
        cargaLista?.cancel()
        _lista.value = ListaUiState.Cargando
        cargaLista = viewModelScope.launch {
            try {
                val datos = repositorio.obtenerTodos()
                _lista.value = if (datos.isEmpty()) ListaUiState.Vacia else ListaUiState.Datos(datos)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _lista.value = ListaUiState.Error("No se pudieron cargar los recordatorios")
            }
        }
    }

    fun cargarDetalle(id: Int) {
        cargaDetalle?.cancel()
        _detalle.value = DetalleUiState.Cargando
        cargaDetalle = viewModelScope.launch {
            try {
                val dato = repositorio.obtenerPorId(id)
                _detalle.value = if (dato == null) DetalleUiState.NoEncontrado else DetalleUiState.Datos(dato)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _detalle.value = DetalleUiState.Error("No se pudo cargar el recordatorio")
            }
        }
    }

    private fun editar(cambio: (CreacionUiState) -> CreacionUiState) {
        val actual = _creacion.value
        if (!actual.guardando && actual.idGuardado == null) {
            _creacion.value = cambio(actual).copy(errorGuardado = null)
        }
    }

    fun cambiarTitulo(valor: String) = editar { it.copy(titulo = valor) }
    fun cambiarDescripcion(valor: String) = editar { it.copy(descripcion = valor) }
    fun cambiarCategoria(valor: Categoria) = editar { it.copy(categoria = valor) }
    fun cambiarPrioridad(valor: Prioridad) = editar { it.copy(prioridad = valor) }

    fun reiniciarCreacion() {
        if (!_creacion.value.guardando) _creacion.value = CreacionUiState()
    }

    fun guardar() {
        val actual = _creacion.value
        if (!actual.puedeGuardar) return
        val categoria = actual.categoria ?: return
        val prioridad = actual.prioridad ?: return
        _creacion.value = actual.copy(guardando = true, errorGuardado = null)
        viewModelScope.launch {
            try {
                val nuevo = repositorio.crear(actual.titulo.trim(), actual.descripcion, categoria, prioridad)
                _creacion.value = actual.copy(idGuardado = nuevo.id)
                cargarLista()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _creacion.value = actual.copy(errorGuardado = "No se pudo guardar el recordatorio")
            }
        }
    }
}
