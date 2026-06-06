# UNIVERSIDAD UTE

<p align="center">
  <img src="https://scontent.fuio11-1.fna.fbcdn.net/v/t39.30808-6/453605587_122095148588461256_7673091839495313886_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=pQVEKPOSboIQ7kNvwFoXicn&_nc_oc=AdpTXDKAIbeexIhVmvA-XHzosMtnmNhdlM0Y-FGOJslUZP_0jTuoL_TAFrN1RTlZ9I8&_nc_zt=23&_nc_ht=scontent.fuio11-1.fna&_nc_gid=u6X12X37qzporUbd51ebYQ&_nc_ss=7a2a8&oh=00_Af_X187yW92QJ91Ef5jzbfFyljVuxMOg7FlgQw9BSvWcpw&oe=6A2799BA" alt="Logo Universidad UTE" width="300"/>
</p>

## ESCUELA DE TECNOLOGÍAS
### CARRERA DE DESARROLLO DE SOFTWARE

---

* **Proyecto:** App Móvil Android — Control de Puerto Marítimo
* **Materia:** Programación IV
* **Estudiante:** Andrés Zambrano
* **Docente:** Ing. Francisco Higuera
* **Fecha:** Junio 2026

---

# Marítimo Control - Sistema de Gestión Portuaria Móvil

**Marítimo Control** es una aplicación móvil nativa desarrollada en Kotlin para Android que permite gestionar, supervisar y controlar las operaciones de un puerto marítimo en tiempo real. La aplicación está diseñada para consumir una API REST robusta construida con Django REST Framework y PostgreSQL.

---

## 🛠️ Arquitectura y Tecnologías Utilizadas

La aplicación ha sido desarrollada siguiendo los estándares más modernos de desarrollo en Android, asegurando escalabilidad, robustez y mantenibilidad:

* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose (Declarative UI)
* **Arquitectura:** MVVM (Model-View-ViewModel) con arquitectura limpia.
* **Inyección de Dependencias:** Dagger Hilt
* **Red y Consumo de API:** Retrofit 2 y OkHttp 3
* **Asincronía:** Coroutines y Flow de Kotlin
* **Almacenamiento Seguro:** TokenDataStore (DataStore Preferences para credenciales JWT)
* **Estilo Visual:** Paleta de colores institucional marítima (Azul Marino, Azul Acero, Gris Claro y tags semánticos de estado).

---

## 📦 Módulos Funcionales (Las 6 Entidades)

El sistema cuenta con 6 módulos independientes, cada uno provisto de su correspondiente flujo CRUD completo (Creación, Lectura, Actualización y Eliminación):

1. **🚢 Buques (HomeScreen):** Gestión de embarcaciones (nombre, matrícula, tipo, capacidad y país de bandera).
2. **⚓ Muelles (MyClassesScreen - Tab Muelles):** Control de los espacios de atraque (código de muelle, capacidad de buques y asociación al puerto).
3. **🌐 Puertos (MyClassesScreen - Tab Puertos):** Administración de los distintos puertos registrados (nombre, ciudad y capacidad máxima).
4. **🧑‍✈️ Capitanes (CatalogScreen / Profile):** Registro del personal de mando de los buques (nombres, apellidos, número de licencia y nacionalidad).
5. **📅 Atraques (GameCenterScreen - Tab Atraques):** Programación y asignación de buques a muelles específicos bajo el mando de un capitán, controlando fechas y horas de entrada/salida.
6. **🔬 Inspecciones (GameCenterScreen - Tab Inspecciones):** Registro de inspecciones de seguridad aplicadas a los atraques (estado de aprobación, fecha y observaciones).

---

## 🛡️ Características Especiales (Cumplimiento de Rúbrica)

* **Búsqueda Reactiva (`?search=`):** Barra de búsqueda dinámica integrada directamente con las consultas al backend de Django para filtrar registros de manera eficiente.
* **Paginación Dinámica ("Cargar más"):** Carga incremental y bajo demanda utilizando paginación con el formato estándar de Django (`results` y control de `next`/`previous` páginas) para optimizar el consumo de recursos de red y memoria.
* **Interceptor de Seguridad JWT (`AuthInterceptor`):** Inyección automática y segura de tokens portadores (`Authorization: Bearer <token>`) en cada petición saliente.
* **Permisos y Roles en UI:** La interfaz oculta de forma dinámica los Floating Action Buttons (FAB), formularios y menús de creación, edición y borrado si el usuario no tiene los roles de `Admin` o `Staff` (modo de **Sólo Lectura** para operadores estándar).

---

## 🚨 Manejo de Errores Resiliente

La aplicación cuenta con una capa de control centralizada (`HttpErrorHandler`) que mapea los códigos HTTP del servidor y proporciona alertas claras mediante Snackbars:

* **`400 Bad Request`:** Informa al usuario sobre datos inválidos o formularios incompletos.
* **`401 Unauthorized / 403 Forbidden`:** Expira la sesión de forma controlada o notifica la falta de permisos.
* **`404 Not Found`:** Notifica que el recurso solicitado ya no existe en el servidor.
* **`500 Internal Server Error`:** Alerta amigable ante inconvenientes técnicos del servidor.
* **Error de Conexión / Red:** Detecta la falta de conectividad a internet de manera reactiva.

---

## 🚀 Instrucciones de Compilación y Ejecución

Para compilar y ejecutar el proyecto localmente, sigue estos pasos:

1. **Requisitos Previos:**
   * Android Studio (versión Jellyfish o superior recomendada).
   * JDK 17 configurado en el entorno de desarrollo.
   * Dispositivo físico Android o Emulador con API 26 (Android 8.0) o superior.

2. **Apertura del Proyecto:**
   * Abre Android Studio.
   * Selecciona **File > Open** y elige el directorio `maritimo-android`.

3. **Configuración del Servidor:**
   * La app está configurada para consumir la API en la URL: `http://zambrano-puertos.uaeftt-ute.site/` (configurable en `local.properties`).

4. **Compilación y Construcción:**
   * Ejecuta **Build > Clean Project** seguido de **Build > Rebuild Project**.
   * Para generar el archivo instalable ejecutable: **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
