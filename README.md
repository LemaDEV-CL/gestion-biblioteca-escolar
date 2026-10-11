# Gestión de Biblioteca Escolar

Proyecto de la EFT de Programación Orientada a Objetos II. Es una aplicación de escritorio en Java para gestionar una biblioteca escolar, con interfaz Swing y almacenamiento en MySQL mediante JDBC.

## Funciones implementadas

- Inicio y cierre de sesión. El acceso utiliza el correo y la contraseña de un usuario registrado en la base de datos.
- Gestión de categorías, libros y estudiantes: creación, consulta, actualización y eliminación.
- Catálogo de libros en modo de solo lectura para quienes no tienen el rol de bibliotecario.
- Registro de préstamos si el estudiante existe y hay stock disponible. El vencimiento se fija a siete días desde el préstamo.
- Registro de devoluciones y actualización del stock. Los préstamos pendientes con vencimiento anterior a la fecha actual se muestran como atrasados.
- Reportes de libros más prestados, historial de un estudiante y libros actualmente prestados.

El menú del bibliotecario incluye categorías, estudiantes y reportes. La vista de préstamos del rol estudiante vincula la sesión con el estudiante mediante el RUT y muestra sus propios préstamos. Las operaciones de préstamo y devolución se ejecutan en hilos separados; el controlador sincroniza los cambios de stock dentro de la aplicación.

## Organización del código

El código está en `src/main/java/cl/duoc/biblioteca`:

- `modelo`: entidades con atributos encapsulados. `Estudiante` y `Usuario` heredan de la clase abstracta `Persona` y sobrescriben `obtenerDescripcion()`.
- `vista`: ventanas Swing.
- `controlador`: validaciones y coordinación de las operaciones.
- `dao` y `dao/impl`: interfaces de acceso a datos e implementaciones JDBC con consultas parametrizadas.
- `conexion`: acceso a MySQL mediante `DatabaseConnection`, que usa una instancia compartida (Singleton).
- `Main`: punto de entrada de la aplicación.

## Requisitos y ejecución

1. Tener un JDK compatible con Java 23 y Maven. El `pom.xml` configura la compilación para Java 23 y la dependencia MySQL Connector/J 9.3.0.
2. Tener MySQL disponible en `localhost:3307`, con la base `biblioteca` y las credenciales configuradas en `DatabaseConnection.java`.
3. Preparar las tablas `usuarios`, `estudiantes`, `categorias`, `libros` y `prestamos`, con las columnas que utilizan los DAO, y al menos un usuario para iniciar sesión. Este repositorio no incluye un script SQL de instalación. La opción `createDatabaseIfNotExist=true` de la conexión no crea las tablas ni los usuarios.
4. Abrir la carpeta como proyecto Maven en el IDE, cargar sus dependencias y ejecutar `cl.duoc.biblioteca.Main`.

Para comprobar la compilación desde la carpeta del proyecto:

```text
mvn compile
```