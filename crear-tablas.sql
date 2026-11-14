-- Crear la base de datos del proyecto.
CREATE DATABASE terramia
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- Seleccionar dónde se crearán las tablas.
USE terramia;

-- Clientes del restaurante.
CREATE TABLE clientes (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    movil VARCHAR(20) NOT NULL,
    correo VARCHAR(254) NOT NULL,

    CONSTRAINT pk_clientes PRIMARY KEY (id),
    CONSTRAINT uq_clientes_movil UNIQUE (movil)
) ENGINE = InnoDB;

-- Reservas asociadas a un cliente.
CREATE TABLE reservas (
    id INT NOT NULL AUTO_INCREMENT,
    cliente_id INT NOT NULL,
    personas INT NOT NULL,
    tronas INT NOT NULL DEFAULT 0,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',

    CONSTRAINT pk_reservas PRIMARY KEY (id),

    CONSTRAINT fk_reservas_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,

    CONSTRAINT chk_reservas_personas
        CHECK (personas BETWEEN 1 AND 12),

    CONSTRAINT chk_reservas_tronas
        CHECK (tronas BETWEEN 0 AND personas),

    CONSTRAINT chk_reservas_estado
        CHECK (estado IN ('ACTIVA', 'CANCELADA', 'FINALIZADA')),

    CONSTRAINT chk_reservas_hora
        CHECK (
            hora BETWEEN '11:30:00' AND '23:30:00'
            AND MINUTE(hora) IN (0, 30)
            AND SECOND(hora) = 0
        )
) ENGINE = InnoDB;

-- Mensajes independientes de las reservas.
CREATE TABLE mensajes_contacto (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(254) NOT NULL,    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL,

    CONSTRAINT pk_mensajes_contacto PRIMARY KEY (id)
) ENGINE = InnoDB;

-- Comprobar las tablas creadas.
SHOW TABLES;