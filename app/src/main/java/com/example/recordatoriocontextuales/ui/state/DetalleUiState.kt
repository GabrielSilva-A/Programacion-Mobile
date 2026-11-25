package com.example.recordatoriocontextuales.ui.state

import com.example.recordatoriocontextuales.model.Recordatorio

sealed interface DetalleUiState {
    data object Inicial : DetalleUiState
    data object Cargando : DetalleUiState
    data class Datos(val recordatorio: Recordatorio) : DetalleUiState
    data object NoEncontrado : DetalleUiState
    data class Error(val mensaje: String) : DetalleUiState
}
