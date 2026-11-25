package com.example.recordatoriocontextuales.ui.state

import com.example.recordatoriocontextuales.model.Categoria
import com.example.recordatoriocontextuales.model.Prioridad

data class CreacionUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: Categoria? = null,
    val prioridad: Prioridad? = null,
    val guardando: Boolean = false,
    val errorGuardado: String? = null,
    val idGuardado: Int? = null
) {
    val errorTitulo: String? get() =
        if (titulo.trim().length < 4) "El título debe tener al menos 4 caracteres" else null
    val errorDescripcion: String? get() =
        if (descripcion.length > 200) "La descripción no puede superar los 200 caracteres" else null
    val errorCategoria: String? get() = if (categoria == null) "Seleccioná una categoría" else null
    val errorPrioridad: String? get() = if (prioridad == null) "Seleccioná una prioridad" else null
    val puedeGuardar: Boolean get() =
        errorTitulo == null && errorDescripcion == null && errorCategoria == null &&
            errorPrioridad == null && !guardando && idGuardado == null
}
