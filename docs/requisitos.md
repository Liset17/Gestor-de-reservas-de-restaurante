# Requisitos

## Acciones del cliente

### Crear reserva 
1. El cliente selecciona la opción "Reservar".
2. Introduce los datos solicitados por la app (nombre, apellidos, móvil y correo).
3. Indica el número de personas para la reserva y de niños menores de 3 años.
4. Selecciona la fecha en el calendario.
5. La app muestra las horas del día, las que no tienen plazas aparecen en gris y no se pueden elegir.
6. El cliente elige una hora y confirma la reserva.
7. La app envía un correo de confirmación.
   
### Consultar reserva
1. El cliente podrá consultar su reserva con su número de móvil en la app.
2. La app envía un código de verificación y el cliente lo introduce.
3. La app muestra sus reservas.
4. Una vez dentro podrá modificar su reserva o cancelarla.
   
### Modificar reserva
1. El cliente debe introducir su número de móvil.
2. La app envía un código de verificación y el cliente lo introduce.
3. La app muestra sus reservas.
4. El cliente tiene que elegir la reserva que quiere modificar.
5. La app muestra las fechas y horas; las ocupadas aparecen en gris.
6. El cliente modifica número de comensales, fecha u hora.
7. El cliente confirma los cambios.

### Cancelar reserva
1. El cliente debe introducir su número de móvil.
2. La app envía un código de verificación y el cliente lo introduce.
3. La app muestra sus reservas.
4. El cliente tiene que elegir la reserva que quiere cancelar.
5. La app pregunta al cliente "¿Seguro que quieres cancelar?".
6. El cliente confirma la anulación de la reserva.
7. La app libera ese horario para otros clientes.

### Enviar mensaje de contacto
1. El cliente abre la sección de contacto.
2. Escribe su nombre, correo y mensaje.
3. La app guarda el mensaje y confirma que se ha enviado.


## Datos de una reserva
Todos los datos son obligatorios.

### Del cliente
- Nombre y apellidos.
- Número de móvil: **identifica al cliente**. Un mismo cliente puede tener varias reservas, cada una identificada mediante su ID de reserva.
- Correo electrónico.

### Del mensaje de contacto
- Nombre.
- Correo electrónico.
- Mensaje.
- Fecha de envío.

### De la reserva
- ID de reserva: identifica cada reserva.
- Número de personas.
- Número de niños menores de 3 años (se mantiene en 0 si no hay).
- Fecha de la reserva.
- Hora de la reserva.
- Estado actual de reserva: activa, cancelada o finalizada.
  

## Reglas del sistema

### Fechas y horarios
- Solo se puede reservar en los horarios fijos del restaurante.
- Las horas van de 30 en 30 minutos, de 11:30 a 23:30, todos los días.
- No se puede reservar en una fecha u hora que ya ha pasado.
- Las horas no disponibles se muestran en gris y no se pueden seleccionar.
- Si no quedan tronas, la app avisa: "No quedan tronas disponibles en este horario. ¿Quieres continuar la reserva sin trona? Si necesitas una silla para el niño, añádelo como persona."
- - Si se continúa sin trona, la reserva mantiene el mismo número de personas. El niño solo ocupa una silla si el cliente lo añade como persona.


### Capacidad
- La aplicación gestionará un máximo de 120 personas reservadas simultáneamente.
- Las 10 plazas restantes del aforo del restaurante quedan fuera del sistema de reservas online.
- Cada reserva ocupa capacidad durante 2 horas. Por ejemplo, una reserva a las 19:00 ocupa capacidad desde las 19:00 hasta las 21:00. A partir de las 21:00, esas plazas vuelven a estar disponibles.
- Una reserva solo se acepta si, durante esas 2 horas, la suma de personas de las reservas gestionadas por la aplicación no supera las 120 plazas disponibles.
- Si no hay sitio, la app avisa al cliente de cuántas plazas quedan libres.
- Las reservas canceladas no ocupan aforo.
- Los niños menores de 3 años que ocupan tronas no cuentan para el aforo.
- Hay 20 tronas, y se cuentan en el mismo tramo de 2 horas que el aforo.
- Si no quedan tronas, la app avisa: "Lo siento, en este horario ya no tenemos tronas disponibles, ¿quiere continuar la reserva sin trona?".



### Reservas del cliente
- El número de personas debe ser entre 1 y 12.
- Grupos de 13 o más personas deben contactar con el restaurante.
- Un cliente no puede tener dos reservas que se pisen en el tiempo.
- Si un cliente ya tiene una reserva ese mismo día, la app le avisa y le pregunta si quiere continuar o modificar la existente.
- Un cliente solo puede ver, modificar o cancelar sus propias reservas. Para acceder a sus reservas, el cliente deberá verificar su identidad mediante un código enviado por SMS o correo electrónico.
- No se puede reservar solo tronas: los niños menores de 3 años siempre van acompañados de al menos 1 persona.


### Modificar y cancelar
- Al modificar una reserva, la app vuelve a comprobar todas las reglas.
- No se puede modificar una reserva cancelada.
- No se puede modificar ni cancelar una reserva que ya ha pasado.
- Una reserva pasa a "finalizada" automáticamente cuando han pasado su fecha y hora.















