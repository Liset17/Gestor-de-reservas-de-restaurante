# Decisiones del proyecto

## 1. Aforo máximo de todo el restaurante
**Decisión:** el aforo se controla por la capacidad del restaurante y no por mesa.
**Por qué:** las mesas se pueden ajustar a la cantidad de personas de la reserva.

## 2. Horarios fijos
**Decisión:** las reservas solo se hacen en horarios fijos del restaurante (19:00, 19:30, 20:00…).
**Por qué:** así el restaurante lleva el control y evita inconsistencias evitando horas sueltas como 19:07.

## 3. El número móvil identifica al cliente
**Decisión:** el número de móvil del cliente se usa para identificar al cliente y encontrar sus reservas.
**Por qué:** el número de móvil es único para cada cliente y permite encontrar sus reservas.

## 4. Cada reserva tendrá su propio ID
**Decisión:** el código de la reserva es independiente del número de móvil del cliente.
**Por qué:** permite que el cliente elija una reserva concreta cuando tenga varias.

## 5. Mostrar solo opciones disponibles
**Decisión:** al crear o modificar, la app solo muestra fechas y horas libres.
**Por qué:** así evita que el cliente desee agendar en horarios completos.

## 6. Aforo de 120 personas en la app
**Decisión:** el aforo máximo en la app es de 120 personas (aunque el establecimiento cuenta con un total de 130).
**Por qué:** el restaurante tiene capacidad máxima en la app para 120 personas porque las otras 10 son por si llegase alguien sin reserva que pase cerca del lugar o si hubiese alguna inconsistencia.

## 7. Duración de 2 horas por reserva
**Decisión:** cada reserva ocupa el aforo durante 2 horas.
**Por qué:** el cliente tiene 1 h 45 min y los 15 min restantes son para preparar la mesa para la siguiente reserva.

## 8. Todos los datos son obligatorios
**Decisión:** nombre, apellidos, móvil y correo son obligatorios.
**Por qué:** de esta manera se puede identificar la reserva del cliente con su nombre y apellidos, y al correo electrónico se le envía la confirmación de su reserva.

## 9. Aviso de reserva el mismo día
**Decisión:** si el cliente ya tiene una reserva ese día, la app le avisa antes de crear otra.
**Por qué:** evita duplicados por error, por ejemplo si el cliente quería cambiar la hora y creó una nueva reserva.

## 10. Web responsive en lugar de app móvil
**Decisión:** la aplicación será una página web que se adapta a ordenador y móvil, no será una app para descargar.
**Por qué:** normalmente, cuando las personas buscan un restaurante en Google Maps, acceden a la página web que aparece allí, y es por donde entra la mayoría de clientes.

## 11. Última reserva a las 23:30
**Decisión:** la última reserva es a las 23:30.
**Por qué:** cada reserva dura 2 horas y el restaurante cierra a la 1:30.

## 12. Máximo 12 personas por reserva
**Decisión:** cada reserva online admite como máximo 12 personas; los grupos de 13 o más contactan con el restaurante.
**Por qué:** los grupos grandes y los eventos necesitan un trato exclusivo y los gestiona el administrador.

## 13. Horas cada 30 minutos
**Decisión:** las horas de reserva van de 30 en 30 minutos.
**Por qué:** facilita el control y evita horas sueltas como las 19:08.

## 14. Horas ocupadas en gris
**Decisión:** las horas sin plazas se ven en gris y no se pueden elegir.
**Por qué:** así el cliente ve todo el horario, pero no puede superar el aforo.

## 15. Estado finalizada automático
**Decisión:** una reserva pasa a «finalizada» automáticamente cuando han pasado 2 horas desde su hora de inicio.
**Por qué:** nadie tiene que marcarlas a mano, y se espera a que termine el tramo de 2 horas porque el cliente sigue en el restaurante durante ese tiempo.

## 16. Los niños menores de 3 años en trona no cuentan para el aforo
**Decisión:** los niños menores de 3 años que usan trona no suman al aforo. Los niños que no usan trona, incluidos los mayores de 3 años, cuentan como personas.
**Por qué:** un bebé en trona no ocupa una plaza ni una silla como un adulto; un niño sin trona sí ocupa una silla.

## 17. 20 tronas por tramo de 2 horas
**Decisión:** hay 20 tronas, que se cuentan por tramos de 2 horas; si no quedan, se puede reservar sin trona.
**Por qué:** las tronas se reutilizan cuando termina cada reserva, y no tener trona no tiene que impedir que la familia reserve.

## 18. Código de verificación simulado en la versión 1
**Decisión:** en la versión 1 el código se simula; el envío real queda para más adelante.
**Por qué:** el código evita que otra persona vea reservas ajenas, pero enviar SMS o correos reales es complejo y tiene coste.

## 19. Formulario de contacto en la versión 1
**Decisión:** el formulario de contacto se incluye en la versión 1.
**Por qué:** los grupos de 13 o más personas y los clientes con dudas necesitan una forma de contactar con el restaurante.

## 20. Zonas y plano de mesas en la versión 2
**Decisión:** las zonas y el plano de mesas se dejan para la versión 2.
**Por qué:** primero tiene que funcionar el aforo total; después se añade la elección de mesa, que es más atractiva para el cliente.

## 21. No se puede reservar solo tronas
**Decisión:** toda reserva debe tener al menos 1 persona; los niños menores de 3 años siempre van acompañados.
**Por qué:** un bebé no puede ir solo al restaurante, y una reserva sin personas no tiene sentido.

## 22. Reserva sin trona
**Decisión:** si no quedan tronas, el sistema no cambia por su cuenta el número de personas. Si el niño va a ocupar una silla, el cliente debe añadirlo como una persona más.
**Por qué:** el sistema no supone nada y es el cliente quien decide; así el aforo refleja exactamente lo que el cliente ha pedido.

## 23. Las tronas las elige el cliente
**Decisión:** el selector de tronas empieza en 0 y es el cliente quien elige cuántas necesita.
**Por qué:** el sistema no puede saber cuántos niños necesitan trona; el cliente sí.

## 24. Solo se pide el número de tronas
**Decisión:** no se pide el número de niños menores de 3 años, solo el número de tronas.
**Por qué:** el número de tronas ya indica cuántos niños las necesitan; pedir los dos datos era repetir información.

## 25. Máximo de tronas por reserva
**Decisión:** el número de tronas no puede ser negativo ni superar el número de personas de la reserva.
**Por qué:** cada bebé va acompañado de un adulto. Se asume que no cubre casos como un adulto solo con gemelos.

## 26. La pantalla pregunta y el servicio valida
**Decisión:** la pantalla (consola o web) muestra los avisos y pregunta al cliente; el servicio solo valida los datos y guarda la reserva.
**Por qué:** así el servicio funciona igual con consola, con web o con una API, sin depender de cómo se muestra la información.

## 27. Doble validación de tronas
**Decisión:** aunque la pantalla ya compruebe las tronas libres, el servicio lo vuelve a comprobar antes de guardar.
**Por qué:** nunca hay que fiarse de los datos que llegan del cliente, y las tronas pueden haberse ocupado entre la pregunta y el guardado.

---

## Limitaciones conocidas

### Condición de carrera (race condition)
Entre la comprobación de plazas o tronas y el guardado, otro usuario podría ocupar las últimas disponibles. En la versión de consola no ocurre, porque solo hay un usuario a la vez. Se resolverá en la fase de Spring Boot + MySQL con transacciones (`@Transactional`).
