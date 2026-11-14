# Diseño de la base de datos de TerraMía

## Objetivo

Guardar de forma permanente los clientes, sus reservas
y los mensajes de contacto.

La base de datos prevista es MySQL.
Este documento describe el diseño; todavía no crea las tablas.

## 1. Tabla clientes

Representa a los clientes del restaurante.

| Columna | Tipo MySQL | Qué guarda |
|---|---|---|
| id | INT | Identificador interno del cliente |
| nombre | VARCHAR(100) | Nombre del cliente |
| apellidos | VARCHAR(150) | Apellidos del cliente |
| movil | VARCHAR(20) | Número móvil utilizado para acceder a sus reservas |
| correo | VARCHAR(254) | Correo electrónico del cliente |

El ID identifica al cliente dentro de la base de datos.
El móvil sigue siendo el dato utilizado en el acceso
mediante código de verificación.

El móvil se guarda como texto para conservar posibles
ceros iniciales y el prefijo internacional.

## 2. Tabla reservas

Representa las reservas realizadas por los clientes.

| Columna | Tipo MySQL | Qué guarda |
|---|---|---|
| id | INT | Identificador de la reserva |
| cliente_id | INT | Identificador del cliente propietario |
| personas | INT | Cantidad de personas de la reserva |
| tronas | INT | Cantidad de tronas solicitadas |
| fecha | DATE | Día de la reserva |
| hora | TIME | Hora de inicio de la reserva |
| estado | VARCHAR(20) | ACTIVA, CANCELADA o FINALIZADA |

El ID de la reserva y el ID del cliente son identificadores
independientes.

No repetimos el nombre, móvil y correo en cada reserva:
esos datos pertenecen a la tabla clientes.

La duración de dos horas se mantiene como regla del servicio.
Por ahora, no necesitamos guardar una hora de fin.

## 3. Tabla mensajes_contacto

Representa los mensajes recibidos mediante el formulario
de contacto.

| Columna | Tipo MySQL | Qué guarda |
|---|---|---|
| id | INT | Identificador del mensaje |
| nombre | VARCHAR(100) | Nombre de quien escribe |
| correo | VARCHAR(254) | Correo de contacto |
| mensaje | TEXT | Contenido del mensaje |
| fecha_envio | DATETIME | Fecha y hora en que se registra el mensaje |

El formulario de contacto puede utilizarse sin tener reservas.
Por eso, el mensaje guarda sus propios datos de contacto
y no necesita estar asociado a un cliente registrado.

## Significado de los tipos

- INT: número entero.
- VARCHAR(n): texto con una longitud máxima de n caracteres.
- TEXT: texto de mayor extensión, utilizado para el mensaje.
- DATE: fecha.
- TIME: hora.
- DATETIME: fecha y hora juntas.


## Relación entre clientes y reservas

La relación entre clientes y reservas es de uno a muchos:

- Un cliente puede tener cero, una o varias reservas.
- Cada reserva pertenece a un único cliente.

La columna reservas.cliente_id hace referencia a clientes.id.

Por ejemplo, si María tiene el ID de cliente 7,
todas sus reservas tendrán cliente_id = 7.
Cada una conservará su propio ID de reserva.

Esto permite guardar los datos del cliente una sola vez
y asociarle sus diferentes reservas.

El ID interno no sustituye el acceso mediante móvil
y código de verificación: identifica al cliente
dentro de la base de datos.

### Mensajes de contacto

Los mensajes de contacto son independientes de las reservas.
No requieren una relación con clientes, porque una persona
puede escribir al restaurante sin haber reservado.

## Próximos pasos

- Definir la relación entre clientes y reservas.
- Definir claves primarias, generación de IDs,
  datos obligatorios, móvil único y claves foráneas.
- Crear las tablas en MySQL y practicar consultas SQL.

Las longitudes de los campos de texto son límites iniciales
del diseño. Se aplicarán de forma coherente en la base de datos
y en las validaciones de la aplicación.