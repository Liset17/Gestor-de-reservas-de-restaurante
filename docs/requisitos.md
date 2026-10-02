# Requisitos

## Acciones del cliente

### Crear reserva 
1. El cliente selecciona la opción "Reservar".
2. Introduce los datos solicitados por la app (nombre, apellidos, móvil y correo).
3. Indica el número de personas para la reserva.
4. Selecciona la fecha en el calendario.
5. La app muestra las horas disponibles para ese día, según los horarios fijos del restaurante (por ejemplo: 19:00 / 19:30 / 20:00).
6. El cliente elige una hora y confirma la reserva.
7. La app envía un correo de confirmación.
   
### Consultar reserva
1. El cliente podrá consultar su reserva con su número de móvil en la app.
2. La app muestra sus reservas.
3. Una vez dentro podrá modificar su reserva o cancelarla.
   
### Modificar reserva
1. El cliente debe introducir su número de móvil.
2. La app muestra sus reservas.
3. El cliente tiene que elegir la reserva que quiere modificar.
4. La app muestra solo los horarios y fechas disponibles.
5. El cliente modifica número de comensales, fecha u hora.
6. El cliente confirma los cambios.

### Cancelar reserva
1. El cliente debe introducir su número de móvil.
2. La app muestra sus reservas.
3. El cliente tiene que elegir la reserva que quiere cancelar.
4. La app pregunta al cliente "¿Seguro que quieres cancelar?".
5. El cliente confirma la anulación de la reserva.
6. La app libera ese horario para otros clientes.


## Datos de una reserva
Todos los datos son obligatorios.

### Del cliente
- Nombre y apellidos.
- Número de móvil: **identifica al cliente**.
- Correo electrónico.
   
### De la reserva
- ID de reserva: identifica cada reserva.
- Número de personas.
- Fecha de la reserva.
- Hora de la reserva.
- Estado actual de reserva (activa o cancelada).


## Reglas del sistema

### Fechas y horarios
- No se puede reservar en una fecha u hora que ya ha pasado.
- Solo se puede reservar en los horarios fijos del restaurante.

### Capacidad
- El restaurante tiene un aforo máximo de 120 personas.
- Cada reserva ocupa el aforo durante 2 horas.
- Una reserva solo se acepta si, durante esas 2 horas, la suma de personas no supera el aforo máximo.
- Si no hay sitio, la app avisa al cliente de cuántas plazas quedan libres.
- Las reservas canceladas no ocupan aforo.

### Reservas del cliente
- El número de personas debe ser mayor que 0.
- Un cliente no puede tener dos reservas que se pisen en el tiempo.
- Si un cliente ya tiene una reserva ese mismo día, la app le avisa y le pregunta si quiere continuar o modificar la existente.
- Un cliente solo puede ver, modificar o cancelar sus propias reservas.

### Modificar y cancelar
- Al modificar una reserva, la app vuelve a comprobar todas las reglas.
- No se puede modificar una reserva cancelada.
- No se puede modificar ni cancelar una reserva que ya ha pasado.
















