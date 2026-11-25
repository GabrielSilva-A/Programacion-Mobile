package com.example.recordatoriocontextuales.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.recordatoriocontextuales.data.ReminderData
import com.example.recordatoriocontextuales.model.Prioridad
import com.example.recordatoriocontextuales.model.Recordatorio

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio, onClick: (() -> Unit)? = null) {

    val estilo = ReminderData.estiloCategoria[recordatorio.categoria]

    val nombreCategoria = estilo?.first
        ?: recordatorio.categoria.etiqueta

    val colorCategoria = estilo?.second
        ?: MaterialTheme.colorScheme.primary

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.Alta -> Color(0xFFC62828)
        Prioridad.Media -> Color(0xFFF57C00)
        Prioridad.Baja -> Color(0xFF2E7D32)
    }

    Card(
        modifier = Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorCategoria)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Categoría: $nombreCategoria",
                    color = colorCategoria
                )

                Text(
                    text = "Prioridad: ${recordatorio.prioridad.etiqueta}",
                    color = colorPrioridad
                )

                Text(
                    text = "Estado: ${recordatorio.estado.etiqueta}"
                )
            }
        }
    }
}
