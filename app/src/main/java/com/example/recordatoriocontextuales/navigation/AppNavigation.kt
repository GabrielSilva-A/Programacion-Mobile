package com.example.recordatoriocontextuales.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recordatoriocontextuales.model.PreferenciasUsuario
import com.example.recordatoriocontextuales.ui.viewmodel.ConfiguracionViewModel
import com.example.recordatoriocontextuales.ui.screens.ConfiguracionRoute
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recordatoriocontextuales.ui.screens.ListaRecordatoriosRoute
import com.example.recordatoriocontextuales.ui.screens.CrearRecordatorioRoute
import com.example.recordatoriocontextuales.ui.screens.DetalleRecordatorioRoute
import com.example.recordatoriocontextuales.ui.viewmodel.RecordatoriosViewModel

@Composable
fun AppNavigation(viewModel: RecordatoriosViewModel, modifier: Modifier = Modifier, configuracion: ConfiguracionViewModel? = null) {
    val preferencias = if (configuracion != null) configuracion.preferencias.collectAsStateWithLifecycle().value else PreferenciasUsuario()
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.LISTA, modifier = modifier) {
        composable(Routes.LISTA) {
            ListaRecordatoriosRoute(viewModel,
                preferencias = preferencias,
                onConfiguracion = configuracion?.let { { navController.navigate(Routes.CONFIGURACION) { launchSingleTop = true } } },
                onCrear = {
                    if (navController.currentDestination?.route == Routes.LISTA) {
                        viewModel.reiniciarCreacion()
                        navController.navigate(Routes.CREAR) { launchSingleTop = true }
                    }
                },
                onDetalle = { id ->
                    if (navController.currentDestination?.route == Routes.LISTA)
                        navController.navigate(Routes.detalle(id)) { launchSingleTop = true }
                })
        }
        if (configuracion != null) composable(Routes.CONFIGURACION) {
            ConfiguracionRoute(configuracion) { navController.popBackStack() }
        }
        composable(Routes.CREAR) {
            CrearRecordatorioRoute(viewModel,
                onGuardado = { navController.popBackStack(Routes.LISTA, false) },
                onCancelar = { navController.popBackStack(Routes.LISTA, false) })
        }
        composable(Routes.DETALLE, arguments = listOf(navArgument(Routes.ID) { type = NavType.IntType })) { entry ->
            DetalleRecordatorioRoute(
                id = requireNotNull(entry.arguments).getInt(Routes.ID),
                viewModel = viewModel,
                onVolver = { navController.popBackStack(Routes.LISTA, false) })
        }
    }
}
