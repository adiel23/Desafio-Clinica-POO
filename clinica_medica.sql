DROP DATABASE IF EXISTS clinica_medica;
CREATE DATABASE IF NOT EXISTS clinica_medica
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE clinica_medica;

-- 1. Tabla: Pacientes
CREATE TABLE IF NOT EXISTS Pacientes (
    codigo_paciente VARCHAR(20) PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    edad INT UNSIGNED NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL,
    tipo_paciente ENUM('Estudiante', 'Docente', 'Administrativo', 'Visitante') NOT NULL,
    estado_paciente VARCHAR(50) NOT NULL DEFAULT 'Activo'
);

-- 2. Tabla: Doctores
CREATE TABLE IF NOT EXISTS Doctores (
    codigo_doctor VARCHAR(20) PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    especialidad ENUM('Medicina General', 'Psicología', 'Nutrición', 'Fisioterapia') NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo_electronico VARCHAR(100) NOT NULL,
    estado_doctor VARCHAR(50) NOT NULL DEFAULT 'Activo'
);

-- 3. Tabla: Citas Médicas
CREATE TABLE IF NOT EXISTS Citas_Medicas (
    codigo_cita VARCHAR(20) PRIMARY KEY,
    codigo_paciente VARCHAR(20) NOT NULL,
    codigo_doctor VARCHAR(20) NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    motivo_consulta TEXT NOT NULL,
    estado_cita ENUM('Programada', 'Atendida', 'Cancelada') NOT NULL DEFAULT 'Programada',
    
    -- Llaves foráneas con integridad referencial
    CONSTRAINT fk_cita_paciente 
        FOREIGN KEY (codigo_paciente) 
        REFERENCES Pacientes(codigo_paciente)
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
        
    CONSTRAINT fk_cita_doctor 
        FOREIGN KEY (codigo_doctor) 
        REFERENCES Doctores(codigo_doctor)
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
);
