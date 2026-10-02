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
