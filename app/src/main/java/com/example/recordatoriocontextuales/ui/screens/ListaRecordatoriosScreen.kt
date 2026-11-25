package com.example.recordatoriocontextuales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    modifier: Modifier = Modifier
) {
    val estado by viewModel.lista.collectAsStateWithLifecycle()
    ListaRecordatorios(
        estado = estado,
        onReintentar = viewModel::cargarLista,
        modifier = modifier
    )
}

@Composable
fun ListaRecordatorios(
    estado: ListaUiState,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
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
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = estado.recordatorios, key = { it.id }) { recordatorio ->
                        TarjetaRecordatorio(recordatorio)
                    }
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
