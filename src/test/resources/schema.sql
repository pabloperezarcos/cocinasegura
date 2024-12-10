CREATE TABLE IF NOT EXISTS RECETAS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    dificultad VARCHAR(50),
    imagen_url VARCHAR(255),
    ingredientes TEXT,
    instrucciones TEXT,
    pasos TEXT,
    pais_de_origen VARCHAR(50),
    tiempo_de_coccion VARCHAR(20),
    tipo_de_cocina VARCHAR(50),
    titulo VARCHAR(100),
    video_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS USUARIOS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(255) NOT NULL UNIQUE,
    correo VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    roles VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS COMENTARIO (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP,
    receta_id BIGINT,
    texto TEXT,
    usuario_id BIGINT,
    valoracion INT,
    FOREIGN KEY (receta_id) REFERENCES RECETAS(ID),
    FOREIGN KEY (usuario_id) REFERENCES USUARIOS(ID)
);

CREATE TABLE IF NOT EXISTS receta_fotos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    foto_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (receta_id) REFERENCES RECETAS(id)
);

CREATE TABLE IF NOT EXISTS receta_videos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (receta_id) REFERENCES RECETAS(id)
);