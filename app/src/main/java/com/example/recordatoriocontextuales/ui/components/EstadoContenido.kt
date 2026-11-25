package com.example.recordatoriocontextuales.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EstadoContenido(
    mensaje: String,
    modifier: Modifier = Modifier,
    cargando: Boolean = false,
    esError: Boolean = false,
    onReintentar: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (cargando) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
        }
        Text(
            text = mensaje,
            textAlign = TextAlign.Center,
            color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        if (onReintentar != null) {
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}
