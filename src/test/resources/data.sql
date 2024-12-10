-- Desactivar las restricciones de claves foráneas en H2
SET
    REFERENTIAL_INTEGRITY FALSE;

-- Ejecutar los DELETE o TRUNCATE
DELETE FROM
    COMENTARIO;

DELETE FROM
    USUARIOS;

DELETE FROM
    RECETAS;

DELETE FROM
    receta_fotos;

DELETE FROM
    receta_videos;

-- O usar TRUNCATE si lo prefieres (comentado por si lo necesitas)
-- TRUNCATE TABLE COMENTARIO;
-- TRUNCATE TABLE USUARIOS;
-- TRUNCATE TABLE RECETAS;
-- Reactivar las restricciones de claves foráneas en H2
SET
    REFERENTIAL_INTEGRITY TRUE;

-- Reiniciar las secuencias de ID en las tablas
ALTER TABLE
    COMENTARIO
ALTER COLUMN
    ID RESTART WITH 1;

ALTER TABLE
    USUARIOS
ALTER COLUMN
    ID RESTART WITH 1;

ALTER TABLE
    RECETAS
ALTER COLUMN
    ID RESTART WITH 1;

ALTER TABLE
    receta_fotos
ALTER COLUMN
    ID RESTART WITH 1;

ALTER TABLE
    receta_videos
ALTER COLUMN
    ID RESTART WITH 1;

-- Insertar primero en la tabla USUARIOS
INSERT INTO
    USUARIOS (nombre_usuario, correo, contrasena, roles)
VALUES
    (
        'testUser',
        'testuser' || CURRENT_TIMESTAMP || '@example.com',
        'testContrasena',
        'USER'
    );

-- Insertar en la tabla RECETAS
INSERT INTO
    RECETAS (
        descripcion,
        dificultad,
        imagen_url,
        ingredientes,
        pais_de_origen,
        tiempo_de_coccion,
        tipo_de_cocina,
        titulo,
        video_url,
        instrucciones
    )
VALUES
    (
        'Receta de tarta de manzana',
        'Media',
        'http://imagen.com/tarta.jpg',
        'Manzana, Harina, Azúcar',
        'España',
        '45',
        'Postre',
        'Tarta de Manzana',
        'http://video.com/tarta.mp4',
        'instrucciones'
    );

-- Insertar en la tabla COMENTARIO (asegurándose de que el usuario existe)
INSERT INTO
    COMENTARIO (fecha, receta_id, texto, usuario_id, valoracion)
VALUES
    (CURRENT_TIMESTAMP, 1, 'Excelente receta', 1, 5);

INSERT INTO
    receta_fotos (receta_id, foto_url)
VALUES
    (1, '/path/to/foto.jpg');

INSERT INTO
    receta_videos (receta_id, video_url)
VALUES
    (1, '/path/to/video.mp4');