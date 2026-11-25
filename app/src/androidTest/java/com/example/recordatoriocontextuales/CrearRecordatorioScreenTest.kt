package com.example.recordatoriocontextuales

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.recordatoriocontextuales.ui.screens.CrearRecordatorioScreen
import com.example.recordatoriocontextuales.ui.state.CreacionUiState
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.Prioridad
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class CrearRecordatorioScreenTest {
    @get:Rule val compose = createComposeRule()

    private fun mostrar(inicial: CreacionUiState, guardar: () -> Unit = {}, cancelar: () -> Unit = {}) {
        compose.setContent {
            var estado by remember { mutableStateOf(inicial) }
            RecordatorioContextualesTheme {
                CrearRecordatorioScreen(estado,
                    { estado = estado.copy(titulo = it) },
                    { estado = estado.copy(descripcion = it) },
                    { estado = estado.copy(categoria = it) },
                    { estado = estado.copy(prioridad = it) },
                    guardar, cancelar)
            }
        }
    }

    private fun valido() = CreacionUiState("Aviso", "", Categoria.Personal, Prioridad.Media)

    @Test fun formularioInvalidoDeshabilitaGuardar() {
        mostrar(CreacionUiState())
        compose.onNodeWithText("El título debe tener al menos 4 caracteres").assertExists()
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().assertIsNotEnabled()
    }

    @Test fun formularioValidoPermiteGuardar() {
        var llamadas = 0
        mostrar(valido(), guardar = { llamadas++ })
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().assertIsEnabled().performClick()
        compose.runOnIdle { assertEquals(1, llamadas) }
    }

    @Test fun guardandoBloqueaAcciones() {
        mostrar(valido().copy(guardando = true))
        compose.onNodeWithText("Guardando recordatorio...").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithText("Cancelar").performScrollTo().assertIsNotEnabled()
    }

    @Test fun errorDeGuardadoPermiteReintentar() {
        mostrar(valido().copy(errorGuardado = "No se pudo guardar el recordatorio"))
        compose.onNodeWithText("No se pudo guardar el recordatorio").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().assertIsEnabled()
    }

    @Test fun descripcionExcesivaImpideGuardar() {
        mostrar(valido().copy(descripcion = "x".repeat(201)))
        compose.onNodeWithText("La descripción no puede superar los 200 caracteres").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().assertIsNotEnabled()
    }
}
