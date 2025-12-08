# Flujo de trabajo: commit, push, release y deploy

Este documento explica el proceso de desarrollo, commit, push, release y deploy en el proyecto JellyfinPlus.

## Resumen rápido
- **Branches que activan workflows**: `main`, `master`.
- **Workflows principales**:
  - `CI` - `.github/workflows/ci.yml`
  - `Docker Build and Push` - `.github/workflows/docker.yml`
  - `Security Scan` - `.github/workflows/security.yml`
  - `Release` - `.github/workflows/release.yml`
  - `Deploy` - `.github/workflows/deploy.yml`

## 1. Commit y Push (local)
- Realiza cambios en el código.
- Ejecuta tests locales y build para validar:
  - `mvn clean test` (o `mvn package`)
- Comandos habituales:
  - `git add .`
  - `git commit -m "mensaje descriptivo"`
  - `git push origin <branch>` (por ejemplo `master` o `main`)
- **Efecto**: Un `push` a `main`/`master` dispara automáticamente `CI`, `Docker Build and Push` y `Security Scan`.

## 2. CI (`.github/workflows/ci.yml`)
- **Trigger**: `push` y `pull_request` sobre `main` / `master`.
- **Acciones principales**:
  - Checkout del código.
  - Configuración de JDK 21 (Temurin) con cache de Maven.
  - `mvn clean compile`
  - `mvn test`
- **Propósito**: Validar compilación y ejecución de pruebas antes de publicar imágenes o releases.

## 3. Docker Build and Push (`.github/workflows/docker.yml`)
- **Trigger**: `push` a `main`/`master`.
- **Requisitos**: Secretos en el repositorio (Settings > Secrets):
  - `DOCKERHUB_USERNAME`
  - `DOCKERHUB_PASSWORD`
- **Acciones**:
  - Login en Docker Hub.
  - Build y push de la imagen `${{ secrets.DOCKERHUB_USERNAME }}/jellyfinplus:latest`.
  - Uso de cache para optimizar builds.
- **Nota**: Asegúrate de que los secretos estén configurados correctamente.

## 4. Security Scan (`.github/workflows/security.yml`)
- **Trigger**: `push`, `pull_request` sobre `main`/`master`, y semanalmente (lunes a las 00:00).
- **Acción**: Ejecuta `mvn org.owasp:dependency-check-maven:check` y sube el reporte `target/dependency-check-report.html` como artifact.
- **Propósito**: Escanear vulnerabilidades en dependencias.

## 5. Release (`.github/workflows/release.yml`)
- **Trigger**: `push` de tags que empiecen con `v` (ej. `v1.0.0`).
- **Permisos**: `contents: write`
- **Acciones**:
  - Checkout del código.
  - Configuración de JDK 21 con cache de Maven.
  - Build: `mvn clean package -DskipTests`
  - Crear Release en GitHub con el tag.
  - Subir el JAR generado (`target/jellyfinplus-1.0-SNAPSHOT.jar`) como asset.
- **Nota**: El JAR se nombra como `jellyfinplus.jar` en el release.

## 6. Deploy (`.github/workflows/deploy.yml`)
- **Trigger**: `workflow_run` del workflow `"Docker Build and Push"` cuando esté `completed` y exitoso.
- **Acciones**:
  - Loggear la variable `WEBHOOKS_STACK_LATEST`.
  - Enviar una solicitud POST al webhook de Portainer para desplegar.
- **Requisitos**: Variable de repositorio (Settings > Variables):
  - `WEBHOOKS_STACK_LATEST` (URL del webhook de Portainer)
- **Nota**: Asegúrate de que la variable esté configurada correctamente en el repositorio.

## Checklist antes de hacer push para liberar/deploy
- [ ] Secretos configurados: `DOCKERHUB_USERNAME`, `DOCKERHUB_PASSWORD`.
- [ ] Variable de repositorio: `WEBHOOKS_STACK_LATEST`.
- [ ] Tests locales pasan (`mvn test`).
- [ ] Para releases: Crear tag con `git tag vX.Y.Z` y `git push --tags`.
- [ ] Verificar que el JAR se genera correctamente en `target/`.

Este flujo asegura un despliegue automatizado y seguro del proyecto.
