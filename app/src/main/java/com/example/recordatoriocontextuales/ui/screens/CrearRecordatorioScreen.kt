package com.example.recordatoriocontextuales.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.Prioridad
import com.example.recordatoriocontextuales.ui.components.SelectorFormulario
import com.example.recordatoriocontextuales.ui.state.CreacionUiState
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import com.example.recordatoriocontextuales.ui.viewmodel.RecordatoriosViewModel

@Composable
fun CrearRecordatorioRoute(
    viewModel: RecordatoriosViewModel,
    onGuardado: (Int) -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.creacion.collectAsStateWithLifecycle()
    val alGuardar by rememberUpdatedState(onGuardado)
    LaunchedEffect(estado.idGuardado) {
        estado.idGuardado?.let { id ->
            viewModel.reiniciarCreacion()
            alGuardar(id)
        }
    }
    val cancelar = {
        viewModel.reiniciarCreacion()
        onCancelar()
    }
    BackHandler { if (!estado.guardando && estado.idGuardado == null) cancelar() }
    CrearRecordatorioScreen(
        estado, viewModel::cambiarTitulo, viewModel::cambiarDescripcion,
        viewModel::cambiarCategoria, viewModel::cambiarPrioridad,
        viewModel::guardar, cancelar, modifier
    )
}

@Composable
fun CrearRecordatorioScreen(
    estado: CreacionUiState,
    onTitulo: (String) -> Unit,
    onDescripcion: (String) -> Unit,
    onCategoria: (Categoria) -> Unit,
    onPrioridad: (Prioridad) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val habilitado = !estado.guardando && estado.idGuardado == null
    Column(
        modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Nuevo recordatorio", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = estado.titulo, onValueChange = onTitulo,
            label = { Text("Título *") }, singleLine = true,
            enabled = habilitado, isError = estado.errorTitulo != null,
            supportingText = { Text(estado.errorTitulo ?: "Título válido") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = estado.descripcion, onValueChange = onDescripcion,
            label = { Text("Descripción") }, minLines = 3,
            enabled = habilitado, isError = estado.errorDescripcion != null,
            supportingText = {
                Text(estado.errorDescripcion ?: "${estado.descripcion.length}/200")
            }, modifier = Modifier.fillMaxWidth()
        )
        SelectorFormulario("Categoría *",
            listOf(Categoria.Personal, Categoria.Trabajo, Categoria.Salud, Categoria.Estudios),
            estado.categoria, { it.etiqueta }, onCategoria, estado.errorCategoria, habilitado)
        SelectorFormulario("Prioridad *",
            listOf(Prioridad.Baja, Prioridad.Media, Prioridad.Alta),
            estado.prioridad, { it.etiqueta }, onPrioridad, estado.errorPrioridad, habilitado)
        estado.errorGuardado?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (estado.guardando) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Text("Guardando recordatorio...")
        }
        Button(onClick = onGuardar, enabled = estado.puedeGuardar, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar recordatorio")
        }
        OutlinedButton(onClick = onCancelar, enabled = habilitado, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CrearPreview() {
    RecordatorioContextualesTheme {
        CrearRecordatorioScreen(CreacionUiState(), {}, {}, {}, {}, {}, {})
    }
}
