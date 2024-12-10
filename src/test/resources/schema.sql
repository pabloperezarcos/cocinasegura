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