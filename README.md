# JellyfinPlus

JellyfinPlus es una aplicación Spring Boot diseñada para interactuar con instancias de Jellyfin, proporcionando una API REST para gestionar series y episodios. Está construida siguiendo la arquitectura hexagonal (puertos y adaptadores) para garantizar la separación de responsabilidades, escalabilidad y facilidad de mantenimiento.

## Características

- **Recuperación de series**: Obtiene todas las series disponibles en la instancia de Jellyfin.
- **Recuperación de episodios**: Obtiene los episodios de una serie específica.
- **Filtrado de contenido descargado**: Identifica series y episodios que están almacenados localmente (descargados).
- **Documentación interactiva**: Swagger UI para explorar y probar la API.
- **Arquitectura escalable**: Diseño hexagonal que facilita la adición de nuevas funcionalidades.

## Arquitectura

La aplicación sigue los principios de la **arquitectura hexagonal** (también conocida como puertos y adaptadores), que promueve la separación entre la lógica de negocio y las dependencias externas. Esto permite que el núcleo de la aplicación sea independiente de frameworks específicos y facilita las pruebas y la evolución.

### Capas

- **Dominio (Domain)**: Contiene las entidades de negocio (Series, Episode), interfaces de repositorio (ports) y lógica pura. Esta capa es independiente de cualquier framework.
- **Aplicación (Application)**: Contiene los servicios de aplicación que orquestan la lógica de negocio, utilizando los puertos definidos en el dominio.
- **Infraestructura (Infrastructure)**: Contiene los adaptadores que implementan los puertos. Incluye controladores REST, clientes externos (como el cliente de Jellyfin) y configuraciones.

### Beneficios de la Arquitectura Hexagonal

- **Testabilidad**: Fácil mocking de dependencias externas.
- **Mantenibilidad**: Cambios en frameworks o APIs externas no afectan el núcleo.
- **Escalabilidad**: Nuevas funcionalidades se pueden agregar sin modificar el código existente.
- **Independencia**: El dominio no depende de Spring, bases de datos o APIs externas.

## Tecnologías

- **Java 21**: Versión moderna de Java con características avanzadas.
- **Spring Boot 3.3.0**: Framework para desarrollo rápido de aplicaciones.
- **SpringDoc OpenAPI**: Generación automática de documentación Swagger.
- **Lombok**: Reducción de código boilerplate con anotaciones.
- **Docker**: Contenedorización para despliegue fácil.
- **JUnit 5 & Mockito**: Pruebas unitarias y de integración.

## Configuración

### Variables de Entorno

Copia el archivo `.env.example` a `.env` y configura tus credenciales:

```bash
cp .env.example .env
```

Edita el archivo `.env` con tus valores:

```dotenv
# Jellyfin Configuration
JELLYFIN_BASE_URL=http://tu-instancia-jellyfin.com/
JELLYFIN_TOKEN=tu_token_de_api
JELLYFIN_USER_NAME=tu_usuario

# Docker Configuration
CONTAINER_NAME=jellyfinplus
IMAGE_NAME=tu_dockerhub_username/jellyfinplus
VERSION=latest
```

**Importante**: El archivo `.env` está en `.gitignore` para proteger tus credenciales. Nunca lo subas al repositorio.

#### Descripción de Variables

- `JELLYFIN_BASE_URL`: URL base de tu instancia de Jellyfin (incluye el `/` final)
- `JELLYFIN_TOKEN`: Token de API de Jellyfin para autenticación
- `JELLYFIN_USER_NAME`: Nombre de usuario de Jellyfin
- `CONTAINER_NAME`: Nombre del contenedor Docker (por defecto: jellyfinplus)
- `IMAGE_NAME`: Nombre de la imagen Docker en formato `usuario/imagen`
- `VERSION`: Versión/tag de la imagen Docker (por defecto: latest)

## Endpoints de la API

La API proporciona los siguientes endpoints para interactuar con Jellyfin:

### Series

- `GET /api/series`: Obtiene todas las series disponibles.
  - Respuesta: Lista de objetos `Series` con id, nombre, descripción y si está descargada.
- `GET /api/series/downloaded`: Obtiene solo las series descargadas localmente.
  - Respuesta: Lista filtrada de series descargadas.

### Episodios

- `GET /api/episodes/series/{seriesId}`: Obtiene todos los episodios de una serie específica.
  - Parámetro: `seriesId` (ID de la serie)
  - Respuesta: Lista de objetos `Episode` con detalles del episodio.
- `GET /api/episodes/downloaded`: Obtiene todos los episodios descargados.
  - Respuesta: Lista de episodios descargados.

### Documentación Interactiva

Una vez ejecutada la aplicación, accede a la documentación Swagger en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

Aquí puedes explorar todos los endpoints, ver los esquemas de respuesta y probar las llamadas directamente.

## Ejecución

### Desarrollo Local

1. Clona el repositorio: `git clone https://github.com/davidperezmillan/jellyfinplus.git`
2. Configura el archivo `.env` con tus credenciales de Jellyfin.
3. Compila: `mvn clean compile`
4. Ejecuta: `mvn spring-boot:run`

### Con Docker

1. Construye la imagen: `docker-compose build`
2. Ejecuta: `docker-compose up`

La aplicación estará disponible en http://localhost:8080

## Pruebas

- **Pruebas unitarias**: `mvn test` (excluye las de integración)
- **Pruebas de integración**: `mvn test -Dtest=JellyfinIntegrationTest` (conecta a la instancia real de Jellyfin)

## Contribución

Para contribuir:
1. Crea una rama para tu feature: `git checkout -b feature/nueva-funcionalidad`
2. Escribe pruebas para tu código.
3. Asegúrate de que todas las pruebas pasen.
4. Envía un pull request.

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo LICENSE para más detalles.

## Logging

La aplicación utiliza **Lombok** con la anotación `@Slf4j` para simplificar el logging:

- **Lombok @Slf4j**: Genera automáticamente el logger en cada clase
- **SLF4J + Logback**: Backend de logging (incluido en Spring Boot)
- **Logs en consola**: Configuración simple sin archivos de log

### Niveles de Log

- **INFO**: Operaciones principales y respuestas de API
- **DEBUG**: Detalles de peticiones HTTP, configuración y procesamiento (nivel de aplicación)
- **TRACE**: Datos completos de respuestas JSON (si se habilita)
- **ERROR**: Errores con stacktrace completo

### Configuración

El logging se configura en `src/main/resources/application.yml`:
- Nivel root: INFO
- Nivel de aplicación: DEBUG
- Salida: Solo consola

Para cambiar niveles de log, modifica el archivo `application.yml`.
