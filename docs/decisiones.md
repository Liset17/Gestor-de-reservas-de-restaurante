# Decisiones del proyecto

## 1. Aforo máximo de todo el restaurante
- **Decisión:** el aforo se controla por la capacidad del restaurante y no por mesa.
- **Por qué:** las mesas se pueden ajustar a la cantidad de personas de la reserva.

## 2. Horarios fijos
- **Decisión:** las reservas solo se hacen en horarios fijos del restaurante (19:00, 19:30, 20:00…).
- **Por qué:** así el restaurante lleva el control y evita inconsistencias evitando horas sueltas como 19:07.
  
## 3. El número móvil identifica al cliente
- **Decisión:** el número de móvil del cliente se usa para identificar al cliente y encontrar sus reservas.
- **Por qué:** el número de móvil es único para cada cliente y permite encontrar sus reservas.

## 4. Cada reserva tendrá su propio ID
- **Decisión:** El código de la reserva es independiente del número de móvil del cliente.
- **Por qué:** Permite que el cliente elija una reserva concreta cuando tenga varias.

## 5. Mostrar solo opciones disponibles
- **Decisión:** al crear o modificar, la app solo muestra fechas y horas libres.
- **Por qué:** así evita que el cliente desee agendar en horarios completos.

## 6. Aforo de 120 personas en la app
- **Decisión:** el aforo máximo en la app es de 120 personas (aunque el establecimiento cuenta con un total de 130).
- **Por qué:** el restaurante tiene capacidad máxima en la app para 120 personas porque para las otras 10, es por si llegase a venir alguien sin reserva que pase cerca del lugar o si llegase haber alguna inconsistencia.

## 7. Duración de 2 horas por reserva
- **Decisión:** Cada reserva ocupa el aforo durante 2 horas.
- **Por qué:** El cliente tiene 1 h 45 min y los 15 min restantes son para preparar la mesa para la siguiente reserva.

## 8. Todos los datos son obligatorios
- **Decisión:** nombre, apellidos, móvil y correo son obligatorios.
- **Por qué:** de esta manera se podrá referir la reserva del cliente con su nombre y apellido, el correo electrónico se le enviará un correo sobre la confirmación de su reserva.

## 9. Aviso de reserva el mismo día
- **Decisión:** si el cliente ya tiene una reserva ese día, la app le avisa antes de crear otra.
- **Por qué:** evita duplicados por error, por ejemplo si el cliente quería cambiar la hora y creó una nueva reserva.

## 10. Web responsive en lugar de app móvil
- **Decisión:** la aplicación será una página web que se adapta a ordenador y móvil, no será una app para descargar.
- **Por qué:** normalmente las personas cuando buscan en Google Maps" un restaurante es allí donde publican sus páginas y es donde la mayoría de clientes acceden.
## 11. Última reserva a las 23:30
- **Decisión:** la última reserva es a las 23:30.
- **Por qué:** cada reserva dura 2 horas y el restaurante cierra a la 1:30.

## 12. Máximo 12 personas por reserva
- **Decisión:** cada reserva online admite como máximo 12 personas; los grupos de 13 o más contactan con el restaurante.
- **Por qué:** los grupos grandes y los eventos necesitan un trato exclusivo y los gestiona el administrador.

## 13. Horas cada 30 minutos
- **Decisión:** las horas de reserva van de 30 en 30 minutos.
- **Por qué:** facilita el control y evita horas sueltas como las 19:08.

## 14. Horas ocupadas en gris
- **Decisión:** las horas sin plazas se ven en gris y no se pueden elegir.
- **Por qué:** así el cliente ve todo el horario, pero no puede superar el aforo.

## 15. Estado finalizada automático
- **Decisión:** una reserva pasa a "finalizada" automáticamente cuando han pasado su fecha y hora.
- **Por qué:** nadie tiene que marcarlas a mano y el cliente no puede modificar reservas pasadas.

## 16. Los niños menores de 3 años no cuentan para el aforo
- **Decisión:** los niños menores de 3 años no suman al aforo; ocupan trona.
- **Por qué:** un bebé en trona no ocupa una plaza ni una silla como un adulto.

## 17. 20 tronas por tramo de 2 horas
- **Decisión:** hay 20 tronas, que se cuentan por tramos de 2 horas; si no quedan, se puede reservar sin trona.
- **Por qué:** las tronas se reutilizan cuando termina cada reserva, y no tener trona no tiene que impedir que la familia reserve.

## 18. Código de verificación simulado en la versión 1
- **Decisión:** en la versión 1 el código se simula; el envío real queda para más adelante.
- **Por qué:** el código evita que otra persona vea reservas ajenas, pero enviar SMS o correos reales es complejo y tiene coste.

## 19. Formulario de contacto en la versión 1
- **Decisión:** el formulario de contacto se incluye en la versión 1.
- **Por qué:** los grupos de 13 o más personas y los clientes con dudas necesitan una forma de contactar con el restaurante.

## 20. Zonas y plano de mesas en la versión 2
- **Decisión:** las zonas y el plano de mesas se dejan para la versión 2.
- **Por qué:** primero tiene que funcionar el aforo total; después se añade la elección de mesa, que es más atractivo para el cliente.

## 21. No se puede reservar solo tronas
- **Decisión:** toda reserva debe tener al menos 1 persona; los niños menores de 3 años siempre van acompañados.
- **Por qué:** un bebé no puede ir solo al restaurante, y una reserva sin personas no tiene sentido.






