# tallerConstruccion

Esta aplicación fue generada usando JHipster 8.11.0. Puedes encontrar documentación y ayuda en [https://www.jhipster.tech/documentation-archive/v8.11.0](https://www.jhipster.tech/documentation-archive/v8.11.0).

## Requisitos Previos

Antes de comenzar, asegúrate de tener instaladas las siguientes herramientas y tecnologías. Puedes encontrar las instrucciones de instalación en [https://www.jhipster.tech/getting-started](https://www.jhipster.tech/getting-started):

- **Java 17**
- **Node.js**
- **Git**
- **Docker**

## Instalación y Configuración

### 1. Clonar el Repositorio

Clona el repositorio desde tu cuenta de GitHub:

```bash
git clone <URL_DEL_REPOSITORIO>
cd taller-construccion
```

### 2. Levantar la Base de Datos MySQL

Asegúrate de tener Docker abierto y ejecutado. Luego, en la terminal del proyecto, ejecuta el siguiente comando para levantar la base de datos MySQL:

```bash
docker compose -f src/main/docker/mysql.yml up -d
```

Este comando iniciará el contenedor de MySQL en segundo plano.

### 3. Instalar Dependencias del Frontend

Instala las dependencias del frontend ejecutando el siguiente comando en la terminal:

```bash
npm install
```

## Ejecutar el Proyecto Localmente

Para ejecutar el proyecto localmente, necesitas abrir **dos terminales simultáneas** en la raíz del proyecto:

### Terminal 1 - Backend

Ejecuta el siguiente comando para levantar el backend:

```bash
./gradlew -x webapp
```

### Terminal 2 - Frontend

Ejecuta el siguiente comando para levantar el frontend:

```bash
npm start
```

Una vez que ambos servicios estén corriendo, deberías poder acceder a la aplicación en:

**http://localhost:9000/**

## Usuarios por Defecto

JHipster crea dos usuarios por defecto:

- **Usuario:** `user` | **Contraseña:** `user`
- **Usuario:** `admin` | **Contraseña:** `admin`

## Entidades

Encontrarás bajo la sección de **Entidades** una entidad **Alumno** que fue creada como prueba con un CRUD básico. Los ejemplos son auto-generados, se pueden eliminar y crear nuevos registros.

El proyecto también incluye las siguientes entidades:

- **Alumno** - Gestión de alumnos
- **Curso** - Gestión de cursos
- **Profesor** - Gestión de profesores

## Estructura del Proyecto

Node es requerido para la generación y recomendado para el desarrollo. `package.json` siempre se genera para una mejor experiencia de desarrollo con prettier, commit hooks, scripts, etc.

En la raíz del proyecto, JHipster genera archivos de configuración para herramientas como git, prettier, eslint, husky y otras que son bien conocidas y puedes encontrar referencias en la web.

La estructura `/src/*` sigue la estructura estándar de Java.

- `.yo-rc.json` - Archivo de configuración de Yeoman
  La configuración de JHipster se almacena en este archivo en la clave `generator-jhipster`. Puedes encontrar `generator-jhipster-*` para configuraciones específicas de blueprints.
- `.jhipster/*.json` - Archivos de configuración de entidades JHipster
- `npmw` - Wrapper para usar npm instalado localmente
  JHipster instala Node y npm localmente usando la herramienta de construcción por defecto. Este wrapper asegura que npm esté instalado localmente y lo usa evitando algunas diferencias que diferentes versiones pueden causar.
- `/src/main/docker` - Configuraciones de Docker para la aplicación y servicios de los que depende la aplicación

## Desarrollo

El sistema de construcción instalará automáticamente la versión recomendada de Node y npm.

Proporcionamos un wrapper para lanzar npm. Solo necesitarás ejecutar este comando cuando cambien las dependencias en [package.json](package.json):

```bash
./npmw install
```

Usamos scripts de npm y [Webpack][] como nuestro sistema de construcción.

## Construcción para Producción

### Empaquetado como JAR

Para construir el JAR final y optimizar la aplicación tallerConstruccion para producción, ejecuta:

```bash
./gradlew -Pprod clean bootJar
```

Esto concatenará y minificará los archivos CSS y JavaScript del cliente. También modificará `index.html` para que haga referencia a estos nuevos archivos.

Para asegurarte de que todo funcionó, ejecuta:

```bash
java -jar build/libs/*.jar
```

Luego navega a [http://localhost:8080](http://localhost:8080) en tu navegador.

### Empaquetado como WAR

Para empaquetar tu aplicación como un WAR para desplegarla en un servidor de aplicaciones, ejecuta:

```bash
./gradlew -Pprod -Pwar clean bootWar
```

## Testing

### Pruebas de Spring Boot

Para ejecutar las pruebas de tu aplicación, ejecuta:

```bash
./gradlew test integrationTest jacocoTestReport
```

### Pruebas del Cliente

Las pruebas unitarias se ejecutan con [Jest][]. Están ubicadas cerca de los componentes y se pueden ejecutar con:

```bash
./npmw test
```

Las pruebas end-to-end de la UI están potenciadas por [Cypress][]. Están ubicadas en [src/test/javascript/cypress](src/test/javascript/cypress) y se pueden ejecutar iniciando Spring Boot en una terminal (`./gradlew bootRun`) y ejecutando las pruebas (`./npmw run e2e`) en una segunda.

## Docker Compose

JHipster genera una serie de archivos de configuración de Docker Compose en la carpeta [src/main/docker/](src/main/docker/) para lanzar servicios de terceros requeridos.

Por ejemplo, para iniciar los servicios requeridos en contenedores Docker, ejecuta:

```bash
docker compose -f src/main/docker/services.yml up -d
```

Para detener y eliminar los contenedores, ejecuta:

```bash
docker compose -f src/main/docker/services.yml down
```

## Recursos Adicionales

- [JHipster Homepage and latest documentation]: https://www.jhipster.tech
- [JHipster 8.11.0 archive]: https://www.jhipster.tech/documentation-archive/v8.11.0
- [Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.11.0/development/
- [Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.11.0/docker-compose
- [Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.11.0/production/
- [Running tests page]: https://www.jhipster.tech/documentation-archive/v8.11.0/running-tests/
- [Code quality page]: https://www.jhipster.tech/documentation-archive/v8.11.0/code-quality/
- [Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.11.0/setting-up-ci/

[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[Jest]: https://jestjs.io
[Cypress]: https://www.cypress.io/
