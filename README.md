# merchant-microservice

Microservicio de **gestión de merchants (comercios)** de Wipay.

Permite crear, consultar y modificar merchants, y saber a qué cliente pertenece cada uno. Forma parte de un sistema de dos microservicios junto a [client-microservice](https://github.com/tareas-practicas-wipay/client-microservice).

## Stack

- Java: 21
- Spring Boot: 4.1.1
- Maven: Wrapper incluido (`mvnw`)
- Lombok: Gestionado por Spring Boot
- MapStruct: 1.6.3
- DynamoDB: wipay-dynamodb-spring-boot-starter 0.2.0-SNAPSHOT (AWS SDK v2 2.55.16)
- Swagger / OpenAPI: springdoc-openapi 3.1.1

## Requisitos previos

- JDK 21
- Docker Desktop y la infraestructura local levantada: ver [infraestructure](https://github.com/tareas-practicas-wipay/infraestructure)
- El starter de DynamoDB instalado en el repositorio Maven local: clonar [dynamodb-spring-boot-starter](https://github.com/tareas-practicas-wipay/dynamodb-spring-boot-starter) y ejecutar `./mvnw clean install`

## Arranque

El servicio arranca en **http://localhost:8082**.

## Endpoints

Documentación interactiva en **http://localhost:8082/swagger-ui.html**.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/merchants` | Crear un merchant |
| `GET` | `/merchants/{id}` | Obtener un merchant por id. Con `?simpleOutput=true` devuelve solo el id |
| `GET` | `/merchants/search/by-name?name=` | Buscar merchants cuyo nombre contiene el texto, sin distinguir mayúsculas |
| `GET` | `/merchants/search/by-client?clientId=` | Obtener los merchants de un cliente |
| `GET` | `/merchants/{id}/client` | Obtener el id del cliente al que pertenece un merchant |

Los dos endpoints de relaciones están en este microservicio porque es el que gestiona los datos de los merchants. Por ahora no se comprueba que el cliente exista: se hará desde el ejercicio 3, llamando a client-microservice con Feign.

## Modelo de datos

Todas las entidades extienden de `MainTable`, que viene del starter de DynamoDB (diseño de tabla única).

| Entidad | Atributos |
|---|---|
| `MainTable` | `PK`, `SK`, `id`, `status`, `gIndex2Pk`, `gIndex3Pk`, `createdDate` |
| `MerchantEntity` | `name`, `address`, `merchantType`, `keywordsSearch` |

`keywordsSearch` no está en el guion: guarda el nombre en minúsculas para buscar sin distinguir mayúsculas.

El merchant no guarda un atributo `clientId`: el cliente al que pertenece está en su `PK`.

### `MerchantType`

| Valor |
|---|
| `MERCHANT_TYPE_PERSONAL_SERVICES` |
| `MERCHANT_TYPE_FINANCIAL_SERVICES` |
| `MERCHANT_TYPE_RETAIL` |
| `MERCHANT_TYPE_ECOMMERCE` |
| `MERCHANT_TYPE_OTHER` |

## Claves en DynamoDB

Cada merchant se guarda dentro de la partición de su cliente.

| Atributo | Valor |
|---|---|
| `PK` | `CLIENT#{clientId}` |
| `SK` | `MERCHANT#{id}` |
| `gIndex2Pk` | `MERCHANT#{id}` |
| `gIndex3Pk` | `ENTITY#MERCHANT` |

| Consulta | Operación |
|---|---|
| Merchant por id | Query sobre `gIndex2` |
| Merchants de un cliente | Query sobre la tabla con `PK = CLIENT#{clientId}` y `SK` que empieza por `MERCHANT#` |
| Merchants por nombre | Query sobre `gIndex3` (`ENTITY#MERCHANT`) con filtro `contains` sobre `keywordsSearch` |

La búsqueda por nombre lee todos los merchants y después filtra, porque DynamoDB no permite buscar texto parcial con una condición de clave. Gracias a `gIndex3`, no lee los clientes ni el resto de la tabla.

## Estructura del proyecto

```
src/main/java/com/wipay/merchant
├── controller   → Controladores REST
├── service      → Interfaces e implementaciones de la lógica de negocio
├── model        → Modelos generales, de entrada y de salida
├── repository   → Acceso a DynamoDB
└── mappers      → Mappers de MapStruct
```

Flujo de una petición:

```
Controller ── Request ──► (mapper) ── Model ──► Service ── (mapper) ── Entity ──► Repository
           ◄─ Response ── (mapper) ◄─ Model ───         ◄── (mapper) ◄─ Entity ───
```

## Seguimiento del estado del proyecto

- [x] Creación del microservicio
- [x] Dependencias: MapStruct, DynamoDB y Swagger
