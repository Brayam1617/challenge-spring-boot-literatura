# Literalura

Literalura es una aplicación de consola desarrollada en Java con Spring Boot que permite consultar libros desde la API de Gutendex, guardar la información en una base de datos PostgreSQL y realizar consultas sobre libros y autores registrados.

## Funcionalidades

La aplicación permite:

- Buscar libros por título desde la API Gutendex
- Guardar libros y autores en la base de datos
- Listar libros registrados
- Listar autores registrados
- Listar autores vivos en un determinado año
- Mostrar la cantidad de libros registrados por idioma

## Tecnologías utilizadas

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Jackson
- Gutendex API

## Estructura del proyecto

El proyecto está organizado en paquetes para separar responsabilidades:

- `model`: entidades `Libro` y `Autor`
- `repository`: interfaces de acceso a datos con JPA
- clases para mapeo JSON:
  - `Datos`
  - `DatosLibro`
  - `DatosAutor`
- `LiteraluraApplication`: clase principal con menú interactivo

## Requisitos

Antes de ejecutar el proyecto, asegurate de tener instalado:

- Java 17 o superior
- Maven
- PostgreSQL
- IntelliJ IDEA u otro IDE compatible

## Base de datos

La aplicación utiliza PostgreSQL.

Debés crear una base de datos llamada:

```sql
literalura
Configuración

En el archivo src/main/resources/application.properties configurá tus datos de conexión:

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

Reemplazá TU_CONTRASEÑA por tu contraseña real de PostgreSQL.

API utilizada

La aplicación consume la API pública de Gutendex:

Libros del Proyecto Gutenberg en formato JSON

Sitio: https://gutendex.com/

Ejemplo de búsqueda:

https://gutendex.com/books/?search=frankenstein
Cómo ejecutar el proyecto

Clonar o descargar el repositorio

Abrir el proyecto en IntelliJ IDEA

Verificar que PostgreSQL esté corriendo

Verificar la configuración del archivo application.properties

Ejecutar la clase principal:

LiteraluraApplication
Menú de opciones

Al iniciar, la aplicación muestra un menú con las siguientes opciones:

1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año
5 - Mostrar cantidad de libros por idioma
0 - Salir
Funcionamiento
Buscar libro por título

Consulta la API Gutendex con el título ingresado por el usuario, toma el primer resultado encontrado y guarda el libro junto con su autor en la base de datos.

Listar libros registrados

Muestra todos los libros almacenados en PostgreSQL.

Listar autores registrados

Muestra todos los autores almacenados en PostgreSQL.

Listar autores vivos en un determinado año

Permite ingresar un año y muestra los autores que estaban vivos en ese momento.

Mostrar cantidad de libros por idioma

Permite consultar cuántos libros hay guardados en un idioma determinado, por ejemplo:

en para inglés

es para español

fr para francés

pt para portugués

Relación entre entidades

El proyecto maneja una relación entre Libro y Autor.

Para simplificar el desafío, aunque la API puede devolver varios autores por libro, la aplicación toma solamente el primer autor de la lista.

Ejemplo de salida
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año
5 - Mostrar cantidad de libros por idioma
0 - Salir

Si el usuario busca frankenstein, la aplicación puede guardar información similar a:

Libro: Frankenstein; Or, The Modern Prometheus

Autor: Mary Wollstonecraft Shelley

Idioma: en

Aprendizajes aplicados

En este proyecto se trabajó con:

Consumo de APIs REST con HttpClient

Construcción de solicitudes con HttpRequest

Manejo de respuestas con HttpResponse

Conversión de JSON a objetos Java con Jackson

Uso de @JsonIgnoreProperties y @JsonAlias

Persistencia de datos con Spring Data JPA

Relaciones entre entidades

Derived queries

Interacción con el usuario mediante consola y Scanner

Posibles mejoras futuras

Evitar duplicados de libros de forma más robusta

Mejorar validaciones de entrada del usuario

Listar libros por idioma en vez de solo contarlos

Mostrar resultados con un formato más amigable

Separar la lógica en clases de servicio

Autor

Proyecto realizado por Brayam Medina como parte del Challenge Literalura de Alura Latam.
