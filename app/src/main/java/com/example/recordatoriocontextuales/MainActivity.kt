package com.example.recordatoriocontextuales

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recordatoriocontextuales.ui.viewmodel.ConfiguracionViewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recordatoriocontextuales.navigation.AppNavigation
import com.example.recordatoriocontextuales.ui.viewmodel.RecordatoriosViewModel
import com.example.recordatoriocontextuales.ui.theme.RecordatorioContextualesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val configuracion: ConfiguracionViewModel = viewModel()
            val preferencias by configuracion.preferencias.collectAsStateWithLifecycle()
            RecordatorioContextualesTheme(darkTheme = preferencias.modoOscuro) {
                val recordatoriosViewModel: RecordatoriosViewModel = viewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(
                        viewModel = recordatoriosViewModel,
                        configuracion = configuracion,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
