
package com.example.recordatoriocontextuales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RecordatorioContextualesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ListaRecordatorios(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ListaRecordatorios(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Mis recordatorios",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        items(
            items = ReminderData.recordatorios,
            key = { it.id }
        ) { recordatorio ->
            TarjetaRecordatorio(recordatorio)
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio) {

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
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(135.dp)
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

@Preview(showBackground = true)
@Composable
fun ListaRecordatoriosPreview() {
    RecordatorioContextualesTheme {
        ListaRecordatorios()
    }
}
