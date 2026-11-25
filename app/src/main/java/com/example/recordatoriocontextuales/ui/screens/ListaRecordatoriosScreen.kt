package com.example.recordatoriocontextuales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.recordatoriocontextuales.data.ReminderData
import com.example.recordatoriocontextuales.ui.components.TarjetaRecordatorio
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme

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

@Preview(showBackground = true)
@Composable
fun ListaRecordatoriosPreview() {
    RecordatorioContextualesTheme {
        ListaRecordatorios()
    }
}
