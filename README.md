# Integración de Keynua en Android (Kotlin)

En el siguiente gráfico, se puede observar el ciclo básico de integración.

![Flow](https://kpublic.s3.amazonaws.com/images/keynua/mobileIntegrationFlow.jpg)

## Descripción

Este repositorio contiene una aplicación de ejemplo escrita en Kotlin para Android. La aplicación demuestra cómo integrar el flujo de firma o identificación de Keynua utilizando un WebView. El objetivo es mostrar cómo implementar y manejar de manera efectiva el flujo de autenticación de Keynua dentro de una aplicación Android.

## Características

- **Permisos de la aplicación**: Antes de inicializar el WebView, la aplicación configura y solicita los permisos necesarios para el uso de la cámara y el micrófono.

- **Integración de WebView**: Implementa cómo cargar el flujo de Keynua en un WebView.

- **Detección de Finalización del Flujo**: La aplicación identifica la finalización del flujo de validación en el WebView. Para ello, deben enviar una URL personalizada como valor del parámetro `eventURL` en la URL de firma. Si se detecta la presencia de `eventURL`, se añadirá un botón "Finalizar" al concluir la validación. Al presionar el botón "Finalizar", la aplicación ejecuta la URL contenida en `eventURL`, añadiendo los siguientes parámetros como parte de la consulta:
  - `identificationId`: ID de la identificación.
  - `contractId`: ID del contrato.
  - `status`: Estado de la validación. Puede ser "done" o "error"
  - `errCode`: Si el status es "error", acá obtendrás el código de error para que puedas decidir como reaccionar. La lista de errores la encontrarás en la sección ["Errores por tipo de Item"](https://keynua.github.io/slate/#errores-por-tipo-de-item) de la documentación.

## Pre-requisitos

- Android Studio 4.0 o superior.
- Acceso a internet para la carga del flujo de Keynua.

## Configuración e Instalación

1. Clona este repositorio.
2. Abre el proyecto en Android Studio.
3. Modifica el valor del Token de usuario en la aplicación
4. Sincroniza y construye el proyecto con Gradle.
5. Ejecuta la aplicación en un dispositivo o emulador Android.

## Uso

1. Navega al WebView en la aplicación.
2. El flujo de Keynua se cargará automáticamente.
3. Completa el proceso en el WebView.
4. Observa la respuesta de la aplicación al finalizar el flujo.

## Contacto

Para consultas, puedes contactarnos a operaciones@keynua.com
