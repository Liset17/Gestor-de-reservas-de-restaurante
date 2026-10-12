# Requisitos

## Acciones del cliente

### Crear reserva

1. El cliente selecciona la opción «Reservar».
2. Introduce los datos solicitados por la app (nombre, apellidos, móvil y correo electrónico).
3. Indica el número de personas.
4. Selecciona la fecha en el calendario.
5. La app muestra las horas del día. Las que no tienen plazas disponibles aparecen en gris y no se pueden elegir.
6. El cliente elige una hora.
7. Elige el número de tronas en el selector (por defecto, 0).

   * Si solicita más tronas de las que quedan disponibles, la app muestra: «Solo quedan X tronas disponibles. ¿Deseas continuar con X tronas?». Si acepta, continúa con esa cantidad. Si rechaza, puede elegir otra cantidad o cancelar.
   * Si no queda ninguna trona disponible, la app muestra: «No quedan tronas disponibles. ¿Deseas continuar sin tronas?». Si acepta, continúa con 0 tronas. Si rechaza, puede elegir otra cantidad o cancelar.
8. El cliente confirma la reserva.
9. La app envía un correo electrónico de confirmación.

> **Nota:** las tronas no se asignan automáticamente: el cliente elige cuántas necesita. La reserva solo se guarda cuando el cliente ha confirmado todos los datos, incluida la cantidad final de tronas.

### Consultar reserva

1. El cliente puede consultar sus reservas introduciendo su número de móvil en la app.
2. La app envía un código de verificación y el cliente lo introduce.
3. La app muestra sus reservas.
4. Una vez dentro, el cliente podrá modificar una reserva o cancelarla.

### Modificar reserva

1. El cliente introduce su número de móvil.
2. La app envía un código de verificación y el cliente lo introduce.
3. La app muestra sus reservas.
4. El cliente elige la reserva que quiere modificar.
5. La app muestra las fechas y horas disponibles. Las horas ocupadas aparecen en gris y no se pueden seleccionar.
6. El cliente modifica el número de personas, el número de tronas, la fecha o la hora.

   * Si cambia la fecha o la hora, la app vuelve a comprobar las tronas disponibles en el nuevo tramo y aplica los mismos avisos que al crear una reserva.
7. El cliente confirma los cambios.

### Cancelar reserva

1. El cliente introduce su número de móvil.
2. La app envía un código de verificación y el cliente lo introduce.
3. La app muestra sus reservas.
4. El cliente elige la reserva que quiere cancelar.
5. La app pregunta: «¿Seguro que quieres cancelar la reserva?».
6. El cliente confirma la cancelación.
7. La app libera ese horario para otros clientes.

### Enviar mensaje de contacto

1. El cliente abre la sección de contacto.
2. Escribe su nombre, correo electrónico y mensaje.
3. La app guarda el mensaje y confirma que se ha enviado.

## Datos de una reserva

Todos los datos solicitados para crear una reserva son obligatorios, excepto el número de tronas, que será 0 por defecto.

### Del cliente

* Nombre y apellidos.
* Número de móvil: identifica al cliente. Un mismo cliente puede tener varias reservas, cada una identificada mediante su ID de reserva.
* Correo electrónico.

### Del mensaje de contacto

* Nombre.
* Correo electrónico.
* Mensaje.
* Fecha de envío.

### De la reserva

* ID de reserva: identifica cada reserva.
* Número de personas.
* Número de tronas (0 por defecto).
* Fecha de la reserva.
* Hora de la reserva.
* Estado actual de la reserva: activa, cancelada o finalizada.

## Reglas del sistema

### Fechas y horarios

* Solo se puede reservar en los horarios fijos del restaurante.
* Las horas van de 30 en 30 minutos, de 11:30 a 23:30, todos los días.
* No se puede reservar en una fecha u hora que ya haya pasado.
* Las horas no disponibles se muestran en gris y no se pueden seleccionar.

### Capacidad

* La aplicación gestionará un máximo de 120 personas reservadas simultáneamente.
* Las 10 plazas restantes del aforo del restaurante quedan fuera del sistema de reservas online.
* Cada reserva ocupa capacidad durante 2 horas. Por ejemplo, una reserva a las 19:00 ocupa capacidad desde las 19:00 hasta las 21:00. A partir de las 21:00, esas plazas vuelven a estar disponibles.
* Una reserva solo se acepta si, durante esas 2 horas, la suma de personas de las reservas gestionadas por la aplicación no supera las 120 plazas disponibles.
* Si no hay sitio, la app avisa al cliente de cuántas plazas quedan libres.
* Las reservas canceladas no ocupan aforo ni consumen tronas.
* Hay un máximo de 20 tronas, que se contabilizan por separado en el mismo tramo de 2 horas que el aforo.
* Los niños menores de 3 años que utilicen una trona no cuentan para el aforo de personas. Las tronas son exclusivamente para niños menores de 3 años. Los niños que no utilicen trona, incluidos los mayores de 3 años, deben contarse dentro del número total de personas de la reserva.

  Ejemplos:
  * 2 adultos y 1 bebé que necesita trona: el cliente indica 2 personas y 1 trona.
  * 2 adultos y 1 niño de 4 años: el cliente indica 3 personas y 0 tronas.
  * 2 adultos y 1 bebé que no necesita trona: el cliente indica 3 personas y 0 tronas.
* Si no quedan suficientes tronas para la cantidad solicitada, la app avisa al cliente y le ofrece continuar con las tronas disponibles o, si no queda ninguna, continuar sin tronas. La reserva solo se guarda cuando el cliente confirma la cantidad final de tronas.
* El número de tronas solicitadas no puede ser negativo ni superar el número de personas de la reserva.
* La cantidad de tronas asignadas tampoco puede superar las tronas disponibles en el horario elegido ni el máximo de 20 tronas del restaurante.

### Reservas del cliente

* El número de personas debe estar entre 1 y 12.
* Los grupos de 13 o más personas deben contactar con el restaurante.
* Un cliente no puede tener dos reservas que se solapen en el tiempo.
* Si un cliente ya tiene una reserva ese mismo día, la app le avisa y le pregunta si quiere continuar o modificar la existente.
* Un cliente solo puede ver, modificar o cancelar sus propias reservas. Para acceder a ellas, deberá verificar su identidad mediante un código enviado por SMS o correo electrónico.
* No se puede reservar solo tronas: debe haber al menos una persona en la reserva.

### Modificar y cancelar

* Al modificar una reserva, la app vuelve a comprobar todas las reglas.
* No se puede modificar una reserva cancelada.
* No se puede modificar ni cancelar una reserva que ya haya pasado.
* Una reserva pasa a «finalizada» automáticamente cuando han transcurrido 2 horas desde su hora de inicio.











