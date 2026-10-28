<div align="center">

# SaladRey — Pedidos de ensaladas en Android

**Aplicación Android nativa en Java para explorar un catálogo de ensaladas, gestionar un carrito y administrar productos con roles de usuario.**

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Room](https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Material Design](https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

<img src="app/src/main/login.png" alt="Ilustración de ensalada usada en la aplicación" width="280">

</div>

## Descripción

SaladRey es una aplicación Android desarrollada como proyecto académico para simular el flujo de pedidos de una tienda de ensaladas. Incluye autenticación local, catálogo de productos, carrito de compras y un módulo de administración para crear, editar y eliminar ensaladas.

La app está construida con Java y componentes de Android Jetpack, y utiliza Room como base de datos local para que el catálogo y los usuarios persistan en el dispositivo.

## Funcionalidades

### Autenticación y sesión

- Registro de usuarios con correo y contraseña.
- Inicio y cierre de sesión.
- Persistencia de sesión con `SharedPreferences`.
- Rol de administrador para acceder al módulo de gestión.

### Catálogo

- Listado de ensaladas con `RecyclerView`.
- Datos de demostración creados automáticamente la primera vez.
- Selección de productos y confirmación para agregarlos al carrito.
- Estado vacío cuando no hay productos disponibles.

### Carrito

- Agregado de productos con control de cantidades.
- Cálculo del subtotal y total.
- Eliminación de ítems y vaciado del carrito.
- Confirmación de pedido con nombre de cliente o mesa.
- Carrito reactivo mediante `LiveData`.

### Administración

- Visible solo para usuarios con rol `admin`.
- CRUD completo de ensaladas (crear, editar y eliminar).
- Actualización automática de la lista al modificar la base de datos.
- Formulario en diálogo para alta y edición.

## Arquitectura

```text
app/src/main/java/com/mr21082pm18077/saladrey/
├── MainActivity.java                 # Drawer, navegación y sesión
├── data/
│   ├── local/                        # Room: entidades, DAOs y base de datos
│   │   ├── AppDatabase.java
│   │   ├── Salad.java
│   │   ├── SaladDao.java
│   │   ├── User.java
│   │   └── UserDao.java
│   └── repo/                         # Repositorios y modelo del carrito
│       ├── AuthRepository.java
│       ├── SaladRepository.java
│       └── CartRepository.java
├── session/SessionManager.java       # Preferencias de sesión
└── ui/
    ├── auth/                         # Login y registro
    ├── catalog/                      # Catálogo y ViewModel
    ├── cart/                         # Carrito, adaptador y ViewModel
    └── manager/                      # Administración de ensaladas
```

La app sigue una separación por capas:

- **UI:** actividades, fragments, adaptadores y ViewModels.
- **Repositorio:** encapsula el acceso a datos y las operaciones de escritura.
- **Datos locales:** Room para usuarios y ensaladas.
- **Sesión:** `SharedPreferences` para conservar el usuario autenticado.

## Stack técnico

| Área | Tecnología |
| --- | --- |
| Lenguaje | Java 11 |
| SDK | `minSdk 30`, `targetSdk 35`, `compileSdk 36` |
| Build | Gradle Kotlin DSL + AGP 8.9.2 |
| Persistencia | Room 2.6.1 |
| Arquitectura | ViewModel + LiveData + Repository |
| Navegación | Navigation Component 2.6 |
| UI | Material Components, RecyclerView, CardView, ViewBinding |
| Imágenes | Glide 4.16 |
| Sesión | SharedPreferences |

## Requisitos

- Android Studio con Android SDK 36 instalado.
- JDK 11 o superior.
- Dispositivo o emulador con Android 11 (API 30) o superior.

## Compilación y ejecución

### Android Studio

1. Abrir la carpeta raíz del proyecto.
2. Sincronizar Gradle.
3. Ejecutar la configuración `app` en un emulador o dispositivo físico.

### Línea de comandos

```bash
./gradlew assembleDebug
```

El APK se genera en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Credenciales de demostración

La base de datos crea un usuario administrador la primera vez que se inicializa:

| Campo | Valor |
| --- | --- |
| Correo | `admin@salad.com` |
| Contraseña | `1234` |

También se insertan tres ensaladas de ejemplo: César, Mediterránea y Veggie Deluxe.

> Las credenciales y el almacenamiento de contraseñas son parte del alcance educativo del proyecto. No deben usarse en producción.

## Decisiones técnicas destacadas

- **Room** como fuente de verdad local, con entidades y DAOs tipados.
- **LiveData** para observar cambios en la base de datos y refrescar la UI automáticamente.
- **ViewModel** para separar la lógica de presentación de las actividades y fragments.
- **Repository Pattern** para aislar el acceso a datos y facilitar futuras integraciones con una API.
- **`ExecutorService`** para ejecutar operaciones de base de datos fuera del hilo principal.
- **ViewBinding** para eliminar `findViewById` y reducir errores de referencia.
- **Navigation Component** y `DrawerLayout` para una navegación consistente.
- **Control de acceso por rol:** el menú de administración solo se muestra a usuarios `admin`.

## Limitaciones conocidas

- Las contraseñas se almacenan en texto plano; debe migrarse a un hash seguro antes de cualquier uso real.
- El carrito vive en memoria y se pierde al cerrar la aplicación.
- No existe backend ni sincronización entre dispositivos.
- `fallbackToDestructiveMigration()` recrea la base de datos al cambiar de versión.
- El proyecto no incluye pruebas de UI más allá de las pruebas de ejemplo generadas por Android Studio.
- No se procesan pagos ni se persisten pedidos.

## Estado del proyecto

Funcional como prototipo académico de catálogo, carrito y administración con persistencia local.

## Autor

**Francisco Javier Peraza** · [GitHub](https://github.com/CodigoPeraza) · [contacto.peraza@gmail.com](mailto:contacto.peraza@gmail.com)
