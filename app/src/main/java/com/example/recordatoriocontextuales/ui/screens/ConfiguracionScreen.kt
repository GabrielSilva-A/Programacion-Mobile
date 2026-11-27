package com.example.recordatoriocontextuales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recordatoriocontextuales.model.*
import com.example.recordatoriocontextuales.ui.viewmodel.ConfiguracionViewModel

@Composable
fun ConfiguracionRoute(viewModel: ConfiguracionViewModel, onVolver: () -> Unit) {
    val preferencias by viewModel.preferencias.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val guardando by viewModel.guardando.collectAsStateWithLifecycle()
    ConfiguracionScreen(preferencias, viewModel::cambiarModoOscuro,
        viewModel::cambiarMostrarCompletados, viewModel::cambiarOrden, onVolver,
        error = error, guardando = guardando)
}

@Composable
fun ConfiguracionScreen(
    preferencias: PreferenciasUsuario,
    onOscuro: (Boolean) -> Unit,
    onCompletados: (Boolean) -> Unit,
    onOrden: (OrdenRecordatorios) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    guardando: Boolean = false
) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Configuración", style = MaterialTheme.typography.headlineMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Modo oscuro", Modifier.weight(1f))
            Switch(preferencias.modoOscuro, onOscuro, enabled = !guardando)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mostrar completados", Modifier.weight(1f))
            Switch(preferencias.mostrarCompletados, onCompletados, enabled = !guardando)
        }
        Text("Ordenar recordatorios por")
        OrdenRecordatorios.entries.forEach { orden ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(preferencias.orden == orden, { onOrden(orden) }, enabled = !guardando)
                Text(if (orden == OrdenRecordatorios.FECHA) "Fecha de vencimiento" else "Prioridad")
            }
        }
        if (guardando) LinearProgressIndicator(Modifier.fillMaxWidth())
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        OutlinedButton(onClick = onVolver, enabled = !guardando) { Text("Volver al listado") }
    }
}
