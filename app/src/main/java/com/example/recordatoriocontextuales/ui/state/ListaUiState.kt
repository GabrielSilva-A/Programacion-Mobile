package com.example.recordatoriocontextuales.ui.state

import com.example.recordatoriocontextuales.model.Recordatorio

sealed interface ListaUiState {
    data object Cargando : ListaUiState
    data object Vacia : ListaUiState
    data class Datos(val recordatorios: List<Recordatorio>) : ListaUiState
    data class Error(val mensaje: String) : ListaUiState
}
