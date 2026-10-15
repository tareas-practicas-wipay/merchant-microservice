# merchant-microservice

Microservicio de **gestión de merchants (comercios)** de Wipay.

Permite crear, consultar y modificar merchants, y saber a qué cliente pertenece cada uno. Forma parte de un sistema de dos microservicios junto a [client-microservice](https://github.com/tareas-practicas-wipay/client-microservice).

## Stack

- Java: 21
- Spring Boot: 4.1.1
- Maven: Wrapper incluido (`mvnw`)
- Lombok: Gestionado por Spring Boot
- MapStruct: 1.6.3
- DynamoDB: wipay-dynamodb-spring-boot-starter 0.1.0-SNAPSHOT (AWS SDK v2 2.55.16)
- Swagger / OpenAPI: springdoc-openapi 3.1.1

## Requisitos previos

- JDK 21
- Docker Desktop y la infraestructura local levantada: ver [infraestructure](https://github.com/tareas-practicas-wipay/infraestructure)

## Arranque

El servicio arranca en **http://localhost:8082**.

## Modelo de datos

Todas las entidades extienden de `MainTable` (diseño de tabla única en DynamoDB).

| Entidad | Atributos |
|---|---|
| `MainTable` | `PK`, `SK`, `id`, `status`, `gIndex2Pk`, `createdDate` |
| `Merchant` | `name`, `address`, `merchantType` |

### `MerchantType`

| Valor |
|---|
| `MERCHANT_TYPE_PERSONAL_SERVICES` |
| `MERCHANT_TYPE_FINANCIAL_SERVICES` |

## Estructura del proyecto

```
src/main/java/com/wipay/merchant
├── controller   → Controladores REST
├── service      → Interfaces e implementaciones de la lógica de negocio
├── model        → Modelos generales, de entrada y de salida
├── repository   → Acceso a DynamoDB
└── mappers      → Mappers de MapStruct
```

## Seguimiento del estado del proyecto

- [x] Creación del microservicio
- [x] Creación del microservicio
- [x] Dependencias: MapStruct, DynamoDB y Swagger
