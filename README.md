# Recordatorios Contextuales

## Descripción

Aplicación Android desarrollada en Kotlin como parte de la Actividad Práctica 1 de Programación Mobile.

El proyecto permite visualizar una lista de recordatorios simulados, organizados por categorías, prioridades y estados.

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Gradle
- Git y GitHub

## Estructura del proyecto

**Models.kt:** contiene los modelos de datos, las categorías, las prioridades y los estados. Incluye validaciones mediante `init` y `require()`.

**ReminderData.kt:** contiene cinco recordatorios de ejemplo y utiliza colecciones de Kotlin:

- `List` para almacenar recordatorios.
- `Set` para obtener categorías únicas.
- `Map` para relacionar categorías con nombres y colores.
- `filter()` para consultar recordatorios según estado o prioridad.

**MainActivity.kt:** contiene la interfaz desarrollada con Jetpack Compose. Utiliza `LazyColumn`, `Card`, `Row`, `Column`, `Box` y `Text`.

## Funcionalidades implementadas

- Visualización de cinco recordatorios.
- Identificación de categorías mediante colores.
- Diferenciación visual de prioridades.
- Visualización del estado de cada recordatorio.
- Validación de identificadores, títulos y coordenadas.
- Desplazamiento vertical mediante `LazyColumn`.

## Pruebas realizadas

- Compilación de desarrollo con `assembleDebug`: exitosa.
- Ejecución en emulador Pixel 7 con Android 15 (API 35).
- Verificación visual de los cinco recordatorios.
- Prueba de validación de latitud inválida, que produjo la excepción esperada `IllegalArgumentException`.

## Alcance

Esta primera versión utiliza datos simulados. No incluye almacenamiento persistente, notificaciones automáticas ni edición de recordatorios.

## Repositorio

https://github.com/GabrielSilva-A/Programacion-Mobile
