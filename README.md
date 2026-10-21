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
- Spring Cloud: 2025.1.2 (OpenFeign 5.0.x), para las llamadas a client-microservice

## Requisitos previos

- JDK 21
- Docker Desktop y la infraestructura local levantada: ver [infraestructure](https://github.com/tareas-practicas-wipay/infraestructure)
- El starter de DynamoDB instalado en el repositorio Maven local: clonar [dynamodb-spring-boot-starter](https://github.com/tareas-practicas-wipay/dynamodb-spring-boot-starter) y ejecutar `./mvnw clean install`
- client-microservice arrancado en `http://localhost:8081` (se configura con `client.service.url`). Sin él no se pueden crear merchants ni consultar los merchants de un cliente

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
| `PUT` | `/merchants/{id}` | Modificar el nombre, la dirección y el tipo de un merchant |

Los dos endpoints de relaciones están en este microservicio porque es el que gestiona los datos de los merchants.

## Errores

Los errores se devuelven con el formato estándar `ProblemDetail` (RFC 9457):

```json
{
  "type": "about:blank",
  "title": "Recurso no encontrado",
  "status": 404,
  "detail": "No existe ningún merchant con id abc",
  "instance": "/merchants/abc"
}
```

| Código | Cuándo |
|---|---|
| `400` | Datos de entrada no válidos, JSON mal formado o un `merchantType` que no existe. En los errores de validación, `errors` indica el motivo de cada campo |
| `404` | El merchant no existe, o el cliente no existe al buscar sus merchants |
| `422` | Se intenta crear un merchant con un `clientId` que no existe |
| `503` | client-microservice no está disponible o no ha respondido a tiempo |
| `500` | Error inesperado. El detalle se registra en el log, no en la respuesta |

## Comunicación con client-microservice

Se hace con Spring Cloud OpenFeign. La interfaz `ClientFeignClient` (paquete `integration`) llama a `GET /clients/{id}?simpleOutput=true` para comprobar que un cliente existe:

- Al crear un merchant: si el cliente no existe, se devuelve `422` y no se guarda nada.
- Al buscar los merchants de un cliente: si el cliente no existe, se devuelve `404` (así se distingue de un cliente sin merchants, que devuelve `[]`).

Si client-microservice no responde, se devuelve `503` y tampoco se guarda nada.

Tiempos de espera (en `application.properties`):

```properties
spring.cloud.openfeign.client.config.client-service.connect-timeout=2000
spring.cloud.openfeign.client.config.client-service.read-timeout=3000
```

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

El cliente de un merchant no se puede cambiar: forma parte de la `PK`, y en DynamoDB la clave primaria de un ítem no se puede modificar. Cambiarlo obligaría a borrar el ítem y crearlo de nuevo en la partición del otro cliente.

## Estructura del proyecto

```
src/main/java/com/wipay/merchant
├── config       → Configuración (Swagger, tabla de DynamoDB)
├── controller   → Controladores REST
├── exception    → Excepciones y manejo global de errores
├── integration  → Clientes Feign hacia otros microservicios
├── mappers      → Mappers de MapStruct
├── model        → Modelos generales, de entrada y de salida
├── repository   → Acceso a DynamoDB
└── service      → Interfaces e implementaciones de la lógica de negocio
```

Flujo de una petición:

```
Controller ── Request ──► (mapper) ── Model ──► Service ── (mapper) ── Entity ──► Repository
           ◄─ Response ── (mapper) ◄─ Model ───         ◄── (mapper) ◄─ Entity ───
```

## Seguimiento del estado del proyecto

- [x] Ejercicio 1: creación del microservicio y dependencias (MapStruct, DynamoDB y Swagger)
- [x] Ejercicio 2: modelos, mappers, repositorio, servicio y endpoints
    - [x] Crear merchant
    - [x] Buscar por id, con salida completa o simple (`simpleOutput`)
    - [x] Buscar por nombre (coincidencia parcial, sin distinguir mayúsculas)
    - [x] Merchants de un cliente y cliente de un merchant
    - [x] Modificar merchant
    - [x] Manejo de errores con `ProblemDetail`
- [x] Ejercicio 3: comprobar con Feign que el cliente existe
- [ ] Ejercicio 4: interceptor con validación de JWT