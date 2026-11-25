package com.example.recordatoriocontextuales

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun esperarTexto(texto: String) {
        compose.waitUntil(10000) {
            compose.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test fun abreDetalleYRegresa() {
        esperarTexto("Entregar informe")
        compose.onNodeWithText("Entregar informe").performClick()
        esperarTexto("Detalle del recordatorio")
        esperarTexto("Finalizar y enviar el informe semanal")
        compose.onNodeWithText("Volver al listado").performClick()
        esperarTexto("Mis recordatorios")
    }

    @Test fun creaRecordatorioYActualizaLista() {
        esperarTexto("Entregar informe")
        compose.onNodeWithText("Nuevo +").performClick()
        compose.onNodeWithText("Título *").performTextInput("Prueba navegación")
        compose.onNodeWithText("Personal").performScrollTo().performClick()
        compose.onNodeWithText("Media").performScrollTo().performClick()
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().performClick()
        esperarTexto("Mis recordatorios")
        esperarTexto("Entregar informe")
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("Prueba navegación"))
        compose.onNodeWithText("Prueba navegación").assertIsDisplayed().performClick()
        esperarTexto("Detalle del recordatorio")
        esperarTexto("Prueba navegación")
    }

    @Test fun cancelarDescartaFormulario() {
        esperarTexto("Entregar informe")
        compose.onNodeWithText("Nuevo +").performClick()
        compose.onNodeWithText("Título *").performTextInput("Borrador")
        compose.onNodeWithText("Cancelar").performScrollTo().performClick()
        esperarTexto("Mis recordatorios")
        compose.onNodeWithText("Nuevo +").performClick()
        compose.onNodeWithText("Borrador").assertDoesNotExist()
        compose.onNodeWithText("Guardar recordatorio").performScrollTo().assertIsNotEnabled()
    }
}
