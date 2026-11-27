package com.example.recordatoriocontextuales

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.recordatoriocontextuales.data.preferences.PreferenciasRepository
import com.example.recordatoriocontextuales.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PreferenciasRepositoryTest {
    @get:Rule val carpeta = TemporaryFolder()

    @Test fun valoresPredeterminados() = runTest {
        val store = PreferenceDataStoreFactory.create(scope = backgroundScope) {
            carpeta.root.resolve("defaults.preferences_pb")
        }
        assertEquals(PreferenciasUsuario(), PreferenciasRepository(store).preferencias.first())
    }

    @Test fun guardaYRecuperaPreferencias() = runTest {
        val store = PreferenceDataStoreFactory.create(scope = backgroundScope) {
            carpeta.root.resolve("saved.preferences_pb")
        }
        val repo = PreferenciasRepository(store)
        repo.cambiarModoOscuro(true)
        repo.cambiarMostrarCompletados(false)
        repo.cambiarOrden(OrdenRecordatorios.PRIORIDAD)
        assertEquals(PreferenciasUsuario(true, false, OrdenRecordatorios.PRIORIDAD),
            PreferenciasRepository(store).preferencias.first())
    }

    @Test fun ordenDesconocidoUsaValorPredeterminado() = runTest {
        val store = PreferenceDataStoreFactory.create(scope = backgroundScope) {
            carpeta.root.resolve("unknown.preferences_pb")
        }
        store.edit { it[stringPreferencesKey("reminder_order")] = "INVALIDO" }
        assertEquals(OrdenRecordatorios.FECHA, PreferenciasRepository(store).preferencias.first().orden)
    }
}
