package com.example.recordatoriocontextuales.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordatoriocontextuales.data.preferences.PreferenciasRepository
import com.example.recordatoriocontextuales.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConfiguracionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = PreferenciasRepository(application)
    val preferencias = repo.preferencias.stateIn(viewModelScope, SharingStarted.Eagerly, PreferenciasUsuario())
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _guardando = MutableStateFlow(false)
    val guardando = _guardando.asStateFlow()

    private fun guardar(accion: suspend () -> Unit) {
        if (_guardando.value) return
        _guardando.value = true
        _error.value = null
        viewModelScope.launch {
            try { accion() }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { _error.value = "No se pudo guardar la preferencia. Volvé a intentarlo." }
            finally { _guardando.value = false }
        }
    }
    fun cambiarModoOscuro(valor: Boolean) = guardar { repo.cambiarModoOscuro(valor) }
    fun cambiarMostrarCompletados(valor: Boolean) = guardar { repo.cambiarMostrarCompletados(valor) }
    fun cambiarOrden(valor: OrdenRecordatorios) = guardar { repo.cambiarOrden(valor) }
}
