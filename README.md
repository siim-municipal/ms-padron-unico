### 📒 MS Padrón Único (`ms-padron-unico/README.md`)

Documentación enfocada en la gestión de datos maestros y PostGIS (si aplica).

```markdown
# 📒 MS Padrón Único

Repositorio central de información de **Sujetos Pasivos (Contribuyentes)** y **Objetos Padronables (Predios, Licencias, Vehículos)**. Este servicio es la "fuente de la verdad" para la identificación de ciudadanos y sus obligaciones.

![Spring Boot](https://img.shields.io/badge/Spring-Boot-green)
![Liquibase](https://img.shields.io/badge/DB-Liquibase-grey)

## 🎯 Funcionalidades

* **Gestión de Contribuyentes:** Personas Físicas y Morales.
* **Padrón Catastral:** Registro de predios, claves catastrales y valores.
* **Vinculación:** Relación 1:N entre Sujetos y Objetos (Un dueño, muchos predios).
* **Validación Fiscal:** Verifica estatus del contribuyente (RFC, CURP, Domicilio Fiscal).

## ⚙️ Variables de Entorno

| Variable | Descripción |
| :--- | :--- |
| `URL_BD_PADRON` | Conexión a BD (Esquema `padron_db`) |
| `SCHEMA_NAME` | Esquema por defecto (ej. `tuxtepec`) |
| `ISSUER_URI_PADRON` | Issuer de Keycloak (Opcional si se usa solo JWK) |

## 🏗️ Estructura de Base de Datos

El servicio utiliza **Multi-tenancy a nivel de columna** (`municipio_id`) para aislar lógicamente los datos entre diferentes municipios si fuera necesario.

* `cat_sujetos_pasivos`: Datos generales.
* `cat_predios`: Información catastral.
* `rel_sujeto_predio`: Tabla pivote de propiedad.

## 🔒 Seguridad

Requiere rol `ROLE_OPERADOR` o `ROLE_ADMIN` para operaciones de escritura.
