package com.example.recordatoriocontextuales

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.recordatoriocontextuales.data.ReminderData
import com.example.recordatoriocontextuales.ui.screens.DetalleRecordatorioScreen
import com.example.recordatoriocontextuales.ui.state.DetalleUiState
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class DetalleRecordatorioScreenTest {
    @get:Rule val compose = createComposeRule()

    private fun mostrar(estado: DetalleUiState, reintentar: () -> Unit = {}, volver: () -> Unit = {}) {
        compose.setContent {
            RecordatorioContextualesTheme { DetalleRecordatorioScreen(estado, reintentar, volver) }
        }
    }

    @Test fun muestraCarga() {
        mostrar(DetalleUiState.Cargando)
        compose.onNodeWithText("Cargando recordatorio...").assertIsDisplayed()
    }

    @Test fun muestraDatos() {
        mostrar(DetalleUiState.Datos(ReminderData.recordatorios.first()))
        compose.onNodeWithText("Entregar informe").assertIsDisplayed()
        compose.onNodeWithText("Prioridad: Alta").assertIsDisplayed()
        compose.onNodeWithText("Finalizar y enviar el informe semanal").performScrollTo().assertIsDisplayed()
    }

    @Test fun muestraCamposOpcionalesAusentes() {
        mostrar(DetalleUiState.Datos(ReminderData.recordatorios[1]))
        compose.onNodeWithText("Sin fecha asignada").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Sin ubicación asignada").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Sin aviso configurado").performScrollTo().assertIsDisplayed()
    }

    @Test fun muestraNoEncontrado() {
        mostrar(DetalleUiState.NoEncontrado)
        compose.onNodeWithText("Recordatorio no encontrado").assertIsDisplayed()
    }

    @Test fun errorPermiteReintentar() {
        var llamadas = 0
        mostrar(DetalleUiState.Error("Error de prueba"), reintentar = { llamadas++ })
        compose.onNodeWithText("Error de prueba").assertIsDisplayed()
        compose.onNodeWithText("Reintentar").performClick()
        compose.runOnIdle { assertEquals(1, llamadas) }
    }

    @Test fun volverEjecutaEvento() {
        var llamadas = 0
        mostrar(DetalleUiState.NoEncontrado, volver = { llamadas++ })
        compose.onNodeWithText("Volver al listado").performClick()
        compose.runOnIdle { assertEquals(1, llamadas) }
    }
}
