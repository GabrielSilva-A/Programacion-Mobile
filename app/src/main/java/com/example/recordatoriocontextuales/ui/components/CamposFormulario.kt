package com.example.recordatoriocontextuales.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun <T> SelectorFormulario(
    titulo: String,
    opciones: List<T>,
    seleccionado: T?,
    etiqueta: (T) -> String,
    onSeleccionar: (T) -> Unit,
    error: String?,
    habilitado: Boolean
) {
    Column(Modifier.fillMaxWidth().selectableGroup()) {
        Text(titulo, style = MaterialTheme.typography.titleMedium)
        opciones.forEach { opcion ->
            Row(
                Modifier.fillMaxWidth().heightIn(min = 48.dp).selectable(
                    selected = seleccionado == opcion,
                    enabled = habilitado,
                    role = Role.RadioButton,
                    onClick = { onSeleccionar(opcion) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = seleccionado == opcion, onClick = null, enabled = habilitado)
                Spacer(Modifier.width(12.dp))
                Text(etiqueta(opcion))
            }
        }
        if (error != null) Text(error, color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall)
    }
}
