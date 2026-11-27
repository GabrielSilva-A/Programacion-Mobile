package com.example.recordatoriocontextuales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.recordatoriocontextuales.model.PreferenciasUsuario
import com.example.recordatoriocontextuales.model.aplicarPreferencias
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recordatoriocontextuales.data.ReminderData
import com.example.recordatoriocontextuales.ui.components.EstadoContenido
import com.example.recordatoriocontextuales.ui.components.TarjetaRecordatorio
import com.example.recordatoriocontextuales.ui.state.ListaUiState
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import com.example.recordatoriocontextuales.ui.viewmodel.RecordatoriosViewModel

@Composable
fun ListaRecordatoriosRoute(
    viewModel: RecordatoriosViewModel,
    modifier: Modifier = Modifier,
    onCrear: (() -> Unit)? = null,
    onDetalle: ((Int) -> Unit)? = null,
    preferencias: PreferenciasUsuario = PreferenciasUsuario(),
    onConfiguracion: (() -> Unit)? = null
) {
    val estado by viewModel.lista.collectAsStateWithLifecycle()
    ListaRecordatorios(
        estado = if (estado is ListaUiState.Datos) {
            val visibles = aplicarPreferencias((estado as ListaUiState.Datos).recordatorios, preferencias)
            if (visibles.isEmpty()) ListaUiState.Vacia else ListaUiState.Datos(visibles)
        } else estado,
        onReintentar = viewModel::cargarLista,
        modifier = modifier,
        onCrear = onCrear,
        onDetalle = onDetalle,
        onConfiguracion = onConfiguracion
    )
}

@Composable
fun ListaRecordatorios(
    estado: ListaUiState,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onCrear: (() -> Unit)? = null,
    onDetalle: ((Int) -> Unit)? = null,
    onConfiguracion: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (onConfiguracion != null) TextButton(onClick = onConfiguracion, modifier = Modifier.align(Alignment.End)) { Text("Configuración") }
        Text(
            text = "Mis recordatorios",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (estado) {
                ListaUiState.Cargando -> EstadoContenido("Cargando recordatorios...", cargando = true)
                ListaUiState.Vacia -> EstadoContenido("No hay recordatorios")
                is ListaUiState.Error -> EstadoContenido(
                    mensaje = estado.mensaje,
                    esError = true,
                    onReintentar = onReintentar
                )
                is ListaUiState.Datos -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = if (onCrear != null) 88.dp else 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = estado.recordatorios, key = { it.id }) { recordatorio ->
                        TarjetaRecordatorio(recordatorio, onClick = onDetalle?.let { abrir -> { abrir(recordatorio.id) } })
                    }
                }
            }
            if (onCrear != null) {
                FloatingActionButton(onClick = onCrear, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
                    Text("Nuevo +")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaDatosPreview() {
    RecordatorioContextualesTheme {
        ListaRecordatorios(ListaUiState.Datos(ReminderData.recordatorios), {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaCargandoPreview() {
    RecordatorioContextualesTheme {
        ListaRecordatorios(ListaUiState.Cargando, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaVaciaPreview() {
    RecordatorioContextualesTheme {
        ListaRecordatorios(ListaUiState.Vacia, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaErrorPreview() {
    RecordatorioContextualesTheme {
        ListaRecordatorios(ListaUiState.Error("No se pudieron cargar los recordatorios"), {})
    }
}
