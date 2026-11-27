package com.example.recordatoriocontextuales.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.recordatoriocontextuales.model.OrdenRecordatorios
import com.example.recordatoriocontextuales.model.PreferenciasUsuario
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.preferenciasDataStore by preferencesDataStore(name = "preferencias_usuario")

class PreferenciasRepository(private val store: DataStore<Preferences>) {
    constructor(context: Context) : this(context.applicationContext.preferenciasDataStore)

    private object Claves {
        val oscuro = booleanPreferencesKey("dark_mode")
        val completados = booleanPreferencesKey("show_completed")
        val orden = stringPreferencesKey("reminder_order")
    }

    val preferencias = store.data.catch { error ->
        if (error is IOException) emit(emptyPreferences()) else throw error
    }.map { datos ->
        PreferenciasUsuario(
            modoOscuro = datos[Claves.oscuro] ?: false,
            mostrarCompletados = datos[Claves.completados] ?: true,
            orden = OrdenRecordatorios.entries.firstOrNull { it.name == datos[Claves.orden] }
                ?: OrdenRecordatorios.FECHA
        )
    }

    suspend fun cambiarModoOscuro(valor: Boolean) {
        store.edit { it[Claves.oscuro] = valor }
    }

    suspend fun cambiarMostrarCompletados(valor: Boolean) {
        store.edit { it[Claves.completados] = valor }
    }

    suspend fun cambiarOrden(valor: OrdenRecordatorios) {
        store.edit { it[Claves.orden] = valor.name }
    }
}
