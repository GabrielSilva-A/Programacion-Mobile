package com.example.recordatoriocontextuales

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.recordatoriocontextuales.data.ReminderData
import com.example.recordatoriocontextuales.ui.screens.ListaRecordatorios
import com.example.recordatoriocontextuales.ui.state.ListaUiState
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue

class ListaRecordatoriosScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun muestraCarga() {
        compose.setContent { RecordatorioContextualesTheme { ListaRecordatorios(ListaUiState.Cargando, {}) } }
        compose.onNodeWithText("Cargando recordatorios...").assertIsDisplayed()
    }

    @Test fun muestraDatos() {
        compose.setContent { RecordatorioContextualesTheme { ListaRecordatorios(ListaUiState.Datos(ReminderData.recordatorios), {}) } }
        compose.onNodeWithText("Entregar informe").assertIsDisplayed()
    }

    @Test fun muestraListaVacia() {
        compose.setContent { RecordatorioContextualesTheme { ListaRecordatorios(ListaUiState.Vacia, {}) } }
        compose.onNodeWithText("No hay recordatorios").assertIsDisplayed()
    }

    @Test fun muestraErrorYPermiteReintentar() {
        var reintento = false
        compose.setContent { RecordatorioContextualesTheme { ListaRecordatorios(ListaUiState.Error("Error de prueba"), { reintento = true }) } }
        compose.onNodeWithText("Error de prueba").assertIsDisplayed()
        compose.onNodeWithText("Reintentar").performClick()
        compose.runOnIdle { assertTrue(reintento) }
    }
}
