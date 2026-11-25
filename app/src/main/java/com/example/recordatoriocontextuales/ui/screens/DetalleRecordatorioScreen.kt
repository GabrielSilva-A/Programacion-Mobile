package com.example.recordatoriocontextuales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recordatoriocontextuales.data.ReminderData
import com.example.recordatoriocontextuales.model.Recordatorio
import com.example.recordatoriocontextuales.ui.components.EstadoContenido
import com.example.recordatoriocontextuales.ui.state.DetalleUiState
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import com.example.recordatoriocontextuales.ui.viewmodel.RecordatoriosViewModel
import java.time.format.DateTimeFormatter

@Composable
fun DetalleRecordatorioRoute(
    id: Int,
    viewModel: RecordatoriosViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.detalle.collectAsStateWithLifecycle()
    var consultaIniciada by remember(id) { mutableStateOf(false) }
    LaunchedEffect(id, viewModel) {
        viewModel.cargarDetalle(id)
        consultaIniciada = true
    }
    val visible = if (!consultaIniciada ||
        (estado is DetalleUiState.Datos && (estado as DetalleUiState.Datos).recordatorio.id != id)
    ) DetalleUiState.Cargando else estado
    DetalleRecordatorioScreen(visible, { viewModel.cargarDetalle(id) }, onVolver, modifier)
}

@Composable
fun DetalleRecordatorioScreen(
    estado: DetalleUiState,
    onReintentar: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("Detalle del recordatorio", style = MaterialTheme.typography.headlineMedium)
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            when (estado) {
                DetalleUiState.Inicial, DetalleUiState.Cargando ->
                    EstadoContenido("Cargando recordatorio...", cargando = true)
                DetalleUiState.NoEncontrado ->
                    EstadoContenido("Recordatorio no encontrado")
                is DetalleUiState.Error ->
                    EstadoContenido(estado.mensaje, esError = true, onReintentar = onReintentar)
                is DetalleUiState.Datos ->
                    ContenidoDetalle(estado.recordatorio, Modifier.fillMaxSize())
            }
        }
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al listado")
        }
    }
}

@Composable
private fun ContenidoDetalle(recordatorio: Recordatorio, modifier: Modifier = Modifier) {
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    Column(modifier.verticalScroll(rememberScrollState()).padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(recordatorio.categoria.etiqueta, color = MaterialTheme.colorScheme.primary)
                Text(recordatorio.titulo, style = MaterialTheme.typography.headlineSmall)
                Text("Prioridad: ${recordatorio.prioridad.etiqueta}")
                Text("Estado: ${recordatorio.estado.etiqueta}")
            }
        }
        CampoDetalle("Descripción", recordatorio.descripcion.ifBlank { "Sin descripción" })
        CampoDetalle("Identificador", recordatorio.id.toString())
        CampoDetalle("Fecha de vencimiento", recordatorio.fechaVencimiento?.format(formato) ?: "Sin fecha asignada")
        CampoDetalle("Ubicación", recordatorio.ubicacion?.let {
            listOfNotNull(it.descripcion?.takeIf { d -> d.isNotBlank() },
                "${it.latitud}, ${it.longitud}").joinToString("\n")
        } ?: "Sin ubicación asignada")
        CampoDetalle("Aviso", recordatorio.aviso?.format(formato) ?: "Sin aviso configurado")
        CampoDetalle("Sincronización", if (recordatorio.sincronizado) "Sincronizado" else "No sincronizado")
    }
}

@Composable
private fun CampoDetalle(etiqueta: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(etiqueta, style = MaterialTheme.typography.titleSmall)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleDatosPreview() {
    RecordatorioContextualesTheme {
        DetalleRecordatorioScreen(DetalleUiState.Datos(ReminderData.recordatorios.first()), {}, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleCargaPreview() {
    RecordatorioContextualesTheme { DetalleRecordatorioScreen(DetalleUiState.Cargando, {}, {}) }
}

@Preview(showBackground = true)
@Composable
private fun DetalleErrorPreview() {
    RecordatorioContextualesTheme { DetalleRecordatorioScreen(DetalleUiState.Error("No se pudo cargar el recordatorio"), {}, {}) }
}

@Preview(showBackground = true)
@Composable
private fun DetalleAusentePreview() {
    RecordatorioContextualesTheme { DetalleRecordatorioScreen(DetalleUiState.NoEncontrado, {}, {}) }
}
