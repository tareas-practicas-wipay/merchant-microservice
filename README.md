# wipay-client

Microservicio de **gestión de merchants (comercios)** de Wipay.

Permite crear, consultar y modificar merchants, y saber a qué cliente pertenece cada uno. Forma parte de un sistema de dos microservicios junto a [wipay-client](https://github.com/TU_USUARIO/wipay-client).
## Stack

- Java: 21
- Spring Boot: 4.1.1
- Maven: Wrapper incluido (`mvnw`)
- Lombok: Gestionado por Spring Boot
- MapStruct: 1.6.3
- DynamoDB: _Pendiente_ (DynamoDB Local con Docker)
- Swagger / OpenAPI: _Pendiente_ (springdoc-openapi)

## Requisitos previos

- JDK 21
- Docker Desktop (necesario a partir de la integración con DynamoDB)

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
