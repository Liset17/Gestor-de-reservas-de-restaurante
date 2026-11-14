# TerraMía — Gestor de reservas de restaurante

Es un proyecto desarrollado en Java para gestionar las reservas de un restaurante.

El objetivo es crear una aplicación web adaptada a ordenador y móvil.
Actualmente, la primera fase funciona mediante un menú de consola.

## Funcionalidades actuales

- Consultar horarios con aforo disponible.
- Crear reservas.
- Consultar las reservas asociadas a un número móvil.
- Acceder a «Mis reservas» mediante un código de verificación simulado.
- Modificar y cancelar reservas con confirmación previa.
- Consultar la disponibilidad de tronas.
- Ofrecer las tronas disponibles y solicitar aceptación cuando
  no hay suficientes para la cantidad solicitada.
- Avisar si el cliente ya tiene una reserva activa ese mismo día.
- Guardar mensajes de contacto.

## Reglas principales

- Entre 1 y 12 personas por reserva.
- Aforo máximo de 120 personas reservadas simultáneamente.
- Disponibilidad máxima de 20 tronas simultáneas.
- Cada reserva ocupa plazas y tronas durante dos horas.
- Horarios de inicio entre las 11:30 y las 23:30,
  en intervalos de 30 minutos.
- No se permiten reservas con fecha u hora pasada.
- Un cliente no puede tener dos reservas que se solapen.
- Solo se pueden modificar o cancelar reservas activas
  que todavía no hayan empezado.
- Una modificación rechazada conserva los datos originales.
- Las reservas canceladas no ocupan plazas ni tronas.

## Tecnologías

- Java 21.
- Maven.
- JUnit 5.
- Git.

## Estructura del proyecto

| Parte | Responsabilidad |
|---|---|
| Model | Representar clientes, reservas y mensajes |
| Repository | Guardar y recuperar datos en memoria |
| Service | Aplicar las reglas del restaurante |
| Main | Mostrar el menú y recoger los datos del usuario |
| Tests | Comprobar el comportamiento y los casos límite |

Código de la aplicación: `src/main/java/com/terramia/`.

Pruebas: `src/test/java/com/terramia/service/`.

## Cómo ejecutar la consola

1. Tener instalado JDK 21.
2. Abrir el proyecto en IntelliJ IDEA como proyecto Maven.
3. Esperar a que termine la importación de las dependencias.
4. Abrir `src/main/java/com/terramia/Main.java`.
5. Ejecutar el método `main`.

El menú permite elegir entre estas opciones:

```text
=== TerraMía ===
1. Reservar
2. Mis reservas
3. Contacto
0. Salir
```

Los horarios disponibles aparecen numerados.
Para seleccionar uno, se introduce el número de la opción,
no la hora escrita.

## Cómo ejecutar las pruebas

Con Maven instalado y disponible en la terminal,
ejecutar desde la raíz del proyecto:

```bash
mvn test
```

También se pueden ejecutar desde IntelliJ IDEA:

1. Abrir `View → Tool Windows → Maven`.
2. Desplegar el proyecto y la sección `Lifecycle`.
3. Ejecutar `test`.

Última ejecución verificada:

- 45 pruebas de reservas.
- 4 pruebas de contacto.
- Total: 49 pruebas aprobadas, sin fallos ni errores.
- Resultado de Maven: `BUILD SUCCESS`.

## Estado actual

La versión de consola está implementada y se ha comprobado
su recorrido básico de creación, consulta, modificación,
cancelación y contacto.

La conexión del frontend con el backend queda para la siguiente fase.

## Limitaciones actuales

- Los datos se guardan en memoria y se pierden al cerrar el programa.
- El código de verificación se muestra en consola:
  no se envían SMS ni correos reales.
- Los mensajes de contacto se guardan en memoria;
  no se envían al restaurante.
- Se comprueba que móvil y correo estén informados,
  pero todavía no se valida su formato.
- Los estados de las reservas se actualizan al consultar
  o refrescar «Mis reservas», sin una tarea en segundo plano.
- La consola permite una interacción a la vez.
  El control de concurrencia para múltiples usuarios queda pendiente.

## Siguiente fase prevista

- Incorporar Spring Boot.
- Guardar los datos de forma permanente en una base de datos.
- Crear una API para conectar el frontend con el backend.
- Incorporar transacciones y control de concurrencia.

## Mejoras futuras

- Incorporar un plano de la terraza y de la planta 1
  para que el cliente pueda elegir mesa.
- Gestionar zonas del restaurante: terraza, planta 1 y planta 2.
- Reservar la planta 2 para grupos, eventos y clientes sin reserva.
- Incorporar el envío real de códigos de verificación
  por correo o WhatsApp.
- Crear un panel de administración para gestionar reservas,
  grupos, eventos y marcar las reservas de clientes
  que no se hayan presentado.
