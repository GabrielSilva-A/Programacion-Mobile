package com.example.recordatoriocontextuales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recordatoriocontextuales.ui.screens.ListaRecordatoriosRoute
import com.example.recordatoriocontextuales.ui.viewmodel.RecordatoriosViewModel
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RecordatorioContextualesTheme {
                val recordatoriosViewModel: RecordatoriosViewModel = viewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ListaRecordatoriosRoute(
                        viewModel = recordatoriosViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
