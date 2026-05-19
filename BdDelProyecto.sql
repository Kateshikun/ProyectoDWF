SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sistemareservasaereas`
--
CREATE DATABASE IF NOT EXISTS `sistemareservasaereas` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `sistemareservasaereas`;

-- --------------------------------------------------------

--
-- Table structure for table `aerolinea`
--

DROP TABLE IF EXISTS `aerolinea`;
CREATE TABLE IF NOT EXISTS `aerolinea` (
                                           `id_aerolinea` bigint NOT NULL AUTO_INCREMENT,
                                           `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                                           `codigo_icao` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
                                           PRIMARY KEY (`id_aerolinea`),
                                           UNIQUE KEY `uk_aerolinea_codigo_icao` (`codigo_icao`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `aerolinea`
--

INSERT INTO `aerolinea` (`id_aerolinea`, `nombre`, `codigo_icao`) VALUES
                                                                      (1, 'Avianca El Salvador', 'TAO'),
                                                                      (2, 'Copa Airlines', 'CMP'),
                                                                      (3, 'TACA Internacional', 'TAI'),
                                                                      (4, 'Volaris El Salvador', 'VOE'),
                                                                      (5, 'Aeroméxico', 'AMX'),
                                                                      (6, 'LATAM Airlines', 'LAN'),
                                                                      (7, 'American Airlines', 'AAL'),
                                                                      (8, 'Delta Air Lines', 'DAL');

-- --------------------------------------------------------

--
-- Table structure for table `aeropuerto`
--

DROP TABLE IF EXISTS `aeropuerto`;
CREATE TABLE IF NOT EXISTS `aeropuerto` (
                                            `id_aeropuerto` bigint NOT NULL AUTO_INCREMENT,
                                            `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                            `ciudad` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                            `pais` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                            `codigo_iata` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
                                            PRIMARY KEY (`id_aeropuerto`),
                                            UNIQUE KEY `uk_aeropuerto_codigo_iata` (`codigo_iata`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `aeropuerto`
--

INSERT INTO `aeropuerto` (`id_aeropuerto`, `nombre`, `ciudad`, `pais`, `codigo_iata`) VALUES
                                                                                          (1, 'Aeropuerto Internacional Monseñor Óscar Arnulfo Romero', 'San Salvador', 'El Salvador', 'SAL'),
                                                                                          (2, 'Aeropuerto Internacional La Aurora', 'Guatemala', 'Guatemala', 'GUA'),
                                                                                          (3, 'Aeropuerto Internacional Toncontín', 'Tegucigalpa', 'Honduras', 'TON'),
                                                                                          (4, 'Aeropuerto Internacional Augusto C. Sandino', 'Managua', 'Nicaragua', 'MGA'),
                                                                                          (5, 'Aeropuerto Internacional Juan Santamaría', 'San José', 'Costa Rica', 'SJO'),
                                                                                          (6, 'Aeropuerto Internacional Tocumen', 'Ciudad de Panamá', 'Panamá', 'PTY'),
                                                                                          (7, 'Aeropuerto Internacional Benito Juárez', 'Ciudad de México', 'México', 'MEX'),
                                                                                          (8, 'Aeropuerto Internacional Miami', 'Miami', 'Estados Unidos', 'MIA'),
                                                                                          (9, 'Aeropuerto Internacional Los Ángeles', 'Los Ángeles', 'Estados Unidos', 'LAX'),
                                                                                          (10, 'Aeropuerto Internacional El Dorado', 'Bogotá', 'Colombia', 'BOG'),
                                                                                          (11, 'Aeropuerto Internacional Jorge Chávez', 'Lima', 'Perú', 'LIM'),
                                                                                          (12, 'Aeropuerto Internacional Santiago Pudahuel', 'Santiago', 'Chile', 'SCL');

-- --------------------------------------------------------

--
-- Table structure for table `avion`
--

DROP TABLE IF EXISTS `avion`;
CREATE TABLE IF NOT EXISTS `avion` (
                                       `id_avion` bigint NOT NULL AUTO_INCREMENT,
                                       `capacidad_pasajeros` int NOT NULL,
                                       `id_aerolinea` bigint NOT NULL,
                                       PRIMARY KEY (`id_avion`),
                                       KEY `fk_avion_aerolinea` (`id_aerolinea`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `avion`
--

INSERT INTO `avion` (`id_avion`, `capacidad_pasajeros`, `id_aerolinea`) VALUES
                                                                            (1, 180, 1),
                                                                            (2, 160, 1),
                                                                            (3, 200, 2),
                                                                            (4, 220, 2),
                                                                            (5, 150, 3),
                                                                            (6, 186, 3),
                                                                            (7, 174, 4),
                                                                            (8, 144, 4),
                                                                            (9, 250, 5),
                                                                            (10, 300, 6),
                                                                            (11, 280, 7),
                                                                            (12, 260, 7),
                                                                            (13, 220, 8),
                                                                            (14, 190, 1),
                                                                            (15, 170, 2);

-- --------------------------------------------------------

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
CREATE TABLE IF NOT EXISTS `pago` (
                                      `id_pago` bigint NOT NULL AUTO_INCREMENT,
                                      `id_reservacion` bigint DEFAULT NULL,
                                      `monto` double NOT NULL,
                                      `fecha_pago` datetime(6) DEFAULT NULL,
                                      PRIMARY KEY (`id_pago`),
                                      UNIQUE KEY `uk_pago_reservacion` (`id_reservacion`),
                                      KEY `fk_pago_reservacion` (`id_reservacion`),
                                      KEY `idx_pago_fecha` (`fecha_pago`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `pago`
--

INSERT INTO `pago` (`id_pago`, `id_reservacion`, `monto`, `fecha_pago`) VALUES
                                                                            (1, 1, 189, '2026-05-18 10:05:00.000000'),
                                                                            (2, 2, 89, '2026-05-18 10:20:00.000000'),
                                                                            (3, 3, 279, '2026-05-18 11:10:00.000000'),
                                                                            (4, 4, 129, '2026-05-18 11:40:00.000000'),
                                                                            (5, 5, 349, '2026-05-18 12:10:00.000000'),
                                                                            (6, 6, 159, '2026-05-18 12:30:00.000000'),
                                                                            (7, 7, 429, '2026-05-18 13:10:00.000000'),
                                                                            (8, 8, 499, '2026-05-18 14:10:00.000000'),
                                                                            (9, 9, 299, '2026-05-18 15:10:00.000000'),
                                                                            (10, 10, 239, '2026-05-18 15:40:00.000000'),
                                                                            (11, 11, 369, '2026-05-18 16:10:00.000000'),
                                                                            (12, 12, 629, '2026-05-18 16:40:00.000000'),
                                                                            (13, 13, 549, '2026-05-18 17:10:00.000000'),
                                                                            (14, 14, 479, '2026-05-18 17:40:00.000000'),
                                                                            (15, 15, 599, '2026-05-18 18:10:00.000000'),
                                                                            (16, 16, 879, '2026-05-18 18:40:00.000000'),
                                                                            (17, 17, 169, '2026-05-18 19:10:00.000000'),
                                                                            (18, 18, 219, '2026-05-18 19:40:00.000000'),
                                                                            (19, 19, 259, '2026-05-18 20:10:00.000000'),
                                                                            (20, 20, 389, '2026-05-18 20:40:00.000000'),
                                                                            (21, 31, 429, '2026-05-19 08:10:00.000000'),
                                                                            (22, 32, 499, '2026-05-19 08:40:00.000000'),
                                                                            (23, 33, 299, '2026-05-19 09:10:00.000000'),
                                                                            (24, 34, 239, '2026-05-19 09:40:00.000000'),
                                                                            (25, 35, 369, '2026-05-19 10:10:00.000000'),
                                                                            (26, 38, 479, '2026-05-19 11:40:00.000000'),
                                                                            (27, 39, 169, '2026-05-19 12:10:00.000000'),
                                                                            (28, 40, 219, '2026-05-19 12:40:00.000000');

-- --------------------------------------------------------

--
-- Table structure for table `pasajero`
--

DROP TABLE IF EXISTS `pasajero`;
CREATE TABLE IF NOT EXISTS `pasajero` (
                                          `id_pasajero` bigint NOT NULL AUTO_INCREMENT,
                                          `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                          `apellido` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                          `pasaporte` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
                                          `id_usuario` bigint DEFAULT NULL,
                                          PRIMARY KEY (`id_pasajero`),
                                          UNIQUE KEY `uk_pasajero_pasaporte` (`pasaporte`),
                                          KEY `fk_pasajero_usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `pasajero`
--

INSERT INTO `pasajero` (`id_pasajero`, `nombre`, `apellido`, `pasaporte`, `id_usuario`) VALUES
                                                                                            (1, 'Jorge', 'López', 'SV1234567', 2),
                                                                                            (2, 'María', 'García', 'SV2345678', 3),
                                                                                            (3, 'Carlos', 'Hernández', 'SV3456789', 4),
                                                                                            (4, 'Lucía', 'Ramírez', 'SV4567890', 5),
                                                                                            (5, 'Roberto', 'Mejía', 'SV5678901', 6),
                                                                                            (6, 'Ana', 'Díaz', 'SV6789012', 7),
                                                                                            (7, 'Fernando', 'Castillo', 'SV7890123', 8),
                                                                                            (8, 'Sofía', 'Navarrete', 'SV8901234', 9),
                                                                                            (9, 'Pedro', 'Argueta', 'SV9012345', 10),
                                                                                            (10, 'Víctor', 'Velásquez', 'SV0123456', 11),
                                                                                            (11, 'María', 'Reyes', 'SV1122334', 12),
                                                                                            (12, 'Juan', 'Pérez', 'GT1234567', NULL),
                                                                                            (13, 'Carmen', 'Morales', 'GT2345678', NULL),
                                                                                            (14, 'Diego', 'Fuentes', 'CR3456789', NULL),
                                                                                            (15, 'Isabel', 'Torres', 'PA4567890', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `reservacion`
--

DROP TABLE IF EXISTS `reservacion`;
CREATE TABLE IF NOT EXISTS `reservacion` (
                                             `id_reservacion` bigint NOT NULL AUTO_INCREMENT,
                                             `id_vuelo` bigint DEFAULT NULL,
                                             `id_pasajero` bigint DEFAULT NULL,
                                             `fecha_reservacion` datetime(6) DEFAULT NULL,
                                             `estado_reserva` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                             `asiento_asignado` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                             PRIMARY KEY (`id_reservacion`),
                                             KEY `fk_reservacion_vuelo` (`id_vuelo`),
                                             KEY `fk_reservacion_pasajero` (`id_pasajero`),
                                             KEY `idx_reservacion_estado` (`estado_reserva`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `reservacion`
--

INSERT INTO `reservacion` (`id_reservacion`, `id_vuelo`, `id_pasajero`, `fecha_reservacion`, `estado_reserva`, `asiento_asignado`) VALUES
                                                                                                                                       (1, 1, 1, '2026-05-18 10:00:00.000000', 'CONFIRMADA', 'A1'),
                                                                                                                                       (2, 1, 2, '2026-05-18 10:15:00.000000', 'CONFIRMADA', 'A2'),
                                                                                                                                       (3, 2, 3, '2026-05-18 11:00:00.000000', 'CONFIRMADA', 'B1'),
                                                                                                                                       (4, 2, 4, '2026-05-18 11:30:00.000000', 'CONFIRMADA', 'B2'),
                                                                                                                                       (5, 3, 5, '2026-05-18 12:00:00.000000', 'CONFIRMADA', 'C1'),
                                                                                                                                       (6, 3, 6, '2026-05-18 12:20:00.000000', 'CONFIRMADA', 'C2'),
                                                                                                                                       (7, 4, 7, '2026-05-18 13:00:00.000000', 'CONFIRMADA', 'D1'),
                                                                                                                                       (8, 5, 8, '2026-05-18 14:00:00.000000', 'CONFIRMADA', 'E1'),
                                                                                                                                       (9, 6, 9, '2026-05-18 15:00:00.000000', 'CONFIRMADA', 'F1'),
                                                                                                                                       (10, 7, 10, '2026-05-18 15:30:00.000000', 'CONFIRMADA', 'G1'),
                                                                                                                                       (11, 8, 1, '2026-05-18 16:00:00.000000', 'CONFIRMADA', 'H1'),
                                                                                                                                       (12, 9, 2, '2026-05-18 16:30:00.000000', 'CONFIRMADA', 'I1'),
                                                                                                                                       (13, 10, 3, '2026-05-18 17:00:00.000000', 'CONFIRMADA', 'J1'),
                                                                                                                                       (14, 11, 4, '2026-05-18 17:30:00.000000', 'CONFIRMADA', 'K1'),
                                                                                                                                       (15, 12, 5, '2026-05-18 18:00:00.000000', 'CONFIRMADA', 'L1'),
                                                                                                                                       (16, 13, 6, '2026-05-18 18:30:00.000000', 'CONFIRMADA', 'M1'),
                                                                                                                                       (17, 14, 7, '2026-05-18 19:00:00.000000', 'CONFIRMADA', 'N1'),
                                                                                                                                       (18, 15, 8, '2026-05-18 19:30:00.000000', 'CONFIRMADA', 'O1'),
                                                                                                                                       (19, 16, 9, '2026-05-18 20:00:00.000000', 'CONFIRMADA', 'P1'),
                                                                                                                                       (20, 17, 10, '2026-05-18 20:30:00.000000', 'CONFIRMADA', 'Q1'),
                                                                                                                                       (21, 18, 1, '2026-05-18 21:00:00.000000', 'PENDIENTE', 'R1'),
                                                                                                                                       (22, 19, 2, '2026-05-18 21:15:00.000000', 'PENDIENTE', 'S1'),
                                                                                                                                       (23, 20, 3, '2026-05-18 21:30:00.000000', 'PENDIENTE', 'T1'),
                                                                                                                                       (24, 1, 11, '2026-05-18 21:45:00.000000', 'PENDIENTE', 'A3'),
                                                                                                                                       (25, 2, 12, '2026-05-18 22:00:00.000000', 'PENDIENTE', 'B3'),
                                                                                                                                       (26, 21, 4, '2026-05-18 22:15:00.000000', 'CANCELADA', 'U1'),
                                                                                                                                       (27, 22, 5, '2026-05-18 22:30:00.000000', 'CANCELADA', 'V1'),
                                                                                                                                       (28, 23, 6, '2026-05-18 22:45:00.000000', 'CANCELADA', 'W1'),
                                                                                                                                       (29, 24, 13, '2026-05-18 23:00:00.000000', 'CANCELADA', 'X1'),
                                                                                                                                       (30, 25, 14, '2026-05-18 23:15:00.000000', 'CANCELADA', 'Y1'),
                                                                                                                                       (31, 4, 15, '2026-05-19 08:00:00.000000', 'CONFIRMADA', 'D2'),
                                                                                                                                       (32, 5, 1, '2026-05-19 08:30:00.000000', 'CONFIRMADA', 'E2'),
                                                                                                                                       (33, 6, 2, '2026-05-19 09:00:00.000000', 'CONFIRMADA', 'F2'),
                                                                                                                                       (34, 7, 3, '2026-05-19 09:30:00.000000', 'CONFIRMADA', 'G2'),
                                                                                                                                       (35, 8, 4, '2026-05-19 10:00:00.000000', 'CONFIRMADA', 'H2'),
                                                                                                                                       (36, 9, 11, '2026-05-19 10:30:00.000000', 'PENDIENTE', 'I2'),
                                                                                                                                       (37, 10, 12, '2026-05-19 11:00:00.000000', 'PENDIENTE', 'J2'),
                                                                                                                                       (38, 11, 13, '2026-05-19 11:30:00.000000', 'CONFIRMADA', 'K2'),
                                                                                                                                       (39, 14, 14, '2026-05-19 12:00:00.000000', 'CONFIRMADA', 'N2'),
                                                                                                                                       (40, 15, 15, '2026-05-19 12:30:00.000000', 'CONFIRMADA', 'O2');

-- --------------------------------------------------------

--
-- Table structure for table `ruta`
--

DROP TABLE IF EXISTS `ruta`;
CREATE TABLE IF NOT EXISTS `ruta` (
                                      `id_ruta` bigint NOT NULL AUTO_INCREMENT,
                                      `id_aeropuerto_origen` bigint NOT NULL,
                                      `id_aeropuerto_destino` bigint NOT NULL,
                                      `distancia_km` double DEFAULT NULL,
                                      `duracion_estimada` time DEFAULT NULL,
                                      PRIMARY KEY (`id_ruta`),
                                      KEY `fk_ruta_origen` (`id_aeropuerto_origen`),
                                      KEY `fk_ruta_destino` (`id_aeropuerto_destino`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `ruta`
--

INSERT INTO `ruta` (`id_ruta`, `id_aeropuerto_origen`, `id_aeropuerto_destino`, `distancia_km`, `duracion_estimada`) VALUES
                                                                                                                         (1, 1, 2, 250, '00:45:00'),
                                                                                                                         (2, 1, 5, 850, '01:30:00'),
                                                                                                                         (3, 1, 6, 1310, '02:15:00'),
                                                                                                                         (4, 1, 7, 1260, '02:30:00'),
                                                                                                                         (5, 1, 8, 1650, '02:45:00'),
                                                                                                                         (6, 2, 5, 1100, '01:50:00'),
                                                                                                                         (7, 5, 6, 810, '01:20:00'),
                                                                                                                         (8, 6, 10, 1150, '01:45:00'),
                                                                                                                         (9, 7, 8, 2430, '03:30:00'),
                                                                                                                         (10, 7, 9, 2500, '03:45:00'),
                                                                                                                         (11, 10, 11, 1870, '03:00:00'),
                                                                                                                         (12, 11, 12, 2450, '03:30:00'),
                                                                                                                         (13, 8, 9, 4100, '05:30:00'),
                                                                                                                         (14, 1, 3, 340, '00:55:00'),
                                                                                                                         (15, 1, 4, 530, '01:10:00'),
                                                                                                                         (16, 3, 5, 580, '01:15:00'),
                                                                                                                         (17, 5, 10, 1160, '01:50:00'),
                                                                                                                         (18, 6, 8, 1860, '03:00:00'),
                                                                                                                         (19, 8, 10, 2440, '03:40:00'),
                                                                                                                         (20, 9, 10, 5600, '07:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `tarifa`
--

DROP TABLE IF EXISTS `tarifa`;
CREATE TABLE IF NOT EXISTS `tarifa` (
                                        `id_tarifa` bigint NOT NULL AUTO_INCREMENT,
                                        `id_vuelo` bigint DEFAULT NULL,
                                        `clase` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `precio` double DEFAULT NULL,
                                        `disponibilidad` int DEFAULT NULL,
                                        PRIMARY KEY (`id_tarifa`),
                                        KEY `fk_tarifa_vuelo` (`id_vuelo`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tarifa`
--

INSERT INTO `tarifa` (`id_tarifa`, `id_vuelo`, `clase`, `precio`, `disponibilidad`) VALUES
                                                                                        (1, 1, 'Economica', 89, 120),
                                                                                        (2, 1, 'Ejecutiva', 189, 20),
                                                                                        (3, 2, 'Economica', 129, 100),
                                                                                        (4, 2, 'Ejecutiva', 279, 15),
                                                                                        (5, 3, 'Economica', 159, 140),
                                                                                        (6, 3, 'Ejecutiva', 349, 18),
                                                                                        (7, 4, 'Economica', 199, 130),
                                                                                        (8, 4, 'Ejecutiva', 429, 16),
                                                                                        (9, 5, 'Economica', 229, 110),
                                                                                        (10, 5, 'Ejecutiva', 499, 14),
                                                                                        (11, 6, 'Economica', 139, 100),
                                                                                        (12, 6, 'Ejecutiva', 299, 12),
                                                                                        (13, 7, 'Economica', 109, 130),
                                                                                        (14, 7, 'Ejecutiva', 239, 16),
                                                                                        (15, 8, 'Economica', 169, 140),
                                                                                        (16, 8, 'Ejecutiva', 369, 18),
                                                                                        (17, 9, 'Economica', 289, 160),
                                                                                        (18, 9, 'Ejecutiva', 629, 20),
                                                                                        (19, 10, 'Economica', 249, 180),
                                                                                        (20, 10, 'Ejecutiva', 549, 22),
                                                                                        (21, 11, 'Economica', 219, 150),
                                                                                        (22, 11, 'Ejecutiva', 479, 18),
                                                                                        (23, 12, 'Economica', 279, 170),
                                                                                        (24, 12, 'Ejecutiva', 599, 20),
                                                                                        (25, 13, 'Economica', 399, 200),
                                                                                        (26, 13, 'Ejecutiva', 879, 24),
                                                                                        (27, 14, 'Economica', 79, 110),
                                                                                        (28, 14, 'Ejecutiva', 169, 14),
                                                                                        (29, 15, 'Economica', 99, 100),
                                                                                        (30, 15, 'Ejecutiva', 219, 12),
                                                                                        (31, 16, 'Economica', 119, 120),
                                                                                        (32, 16, 'Ejecutiva', 259, 14),
                                                                                        (33, 17, 'Economica', 179, 140),
                                                                                        (34, 17, 'Ejecutiva', 389, 16),
                                                                                        (35, 18, 'Economica', 209, 130),
                                                                                        (36, 18, 'Ejecutiva', 459, 15),
                                                                                        (37, 19, 'Economica', 299, 160),
                                                                                        (38, 19, 'Ejecutiva', 649, 18),
                                                                                        (39, 20, 'Economica', 449, 200),
                                                                                        (40, 20, 'Ejecutiva', 979, 22),
                                                                                        (41, 21, 'Economica', 89, 100),
                                                                                        (42, 21, 'Ejecutiva', 189, 12),
                                                                                        (43, 22, 'Economica', 129, 90),
                                                                                        (44, 22, 'Ejecutiva', 279, 10),
                                                                                        (45, 23, 'Economica', 229, 80),
                                                                                        (46, 23, 'Ejecutiva', 499, 8),
                                                                                        (47, 24, 'Economica', 159, 0),
                                                                                        (48, 24, 'Ejecutiva', 349, 0),
                                                                                        (49, 25, 'Economica', 289, 0),
                                                                                        (50, 25, 'Ejecutiva', 629, 0);

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
                                         `id_usuario` bigint NOT NULL AUTO_INCREMENT,
                                         `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `rol` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `estado` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                         PRIMARY KEY (`id_usuario`),
                                         UNIQUE KEY `uk_usuario_username` (`username`),
                                         UNIQUE KEY `uk_usuario_email` (`email`),
                                         KEY `idx_usuario_rol` (`rol`),
                                         KEY `idx_usuario_estado` (`estado`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `username`, `password`, `email`, `rol`, `estado`) VALUES
                                                                                           (1, 'admin', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'admin@aerolinea.com', 'ADMIN', 'ACTIVO'),
                                                                                           (2, 'jlopez', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'jorge.lopez@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (3, 'mgarcia', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'maria.garcia@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (4, 'chernandez', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'carlos.hernandez@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (5, 'lramirez', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'lucia.ramirez@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (6, 'rmejia', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'roberto.mejia@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (7, 'adiaz', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'ana.diaz@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (8, 'fcastillo', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'fernando.castillo@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (9, 'snavarrete', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'sofia.navarrete@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (10, 'pargueta', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'pedro.argueta@email.com', 'CLIENTE', 'ACTIVO'),
                                                                                           (11, 'velasquez', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'victor.velasquez@email.com', 'CLIENTE', 'INACTIVO'),
                                                                                           (12, 'mreyes', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'maria.reyes@email.com', 'CLIENTE', 'INACTIVO'),
                                                                                           (13, 'admin2', '$2a$05$Xq7U3F1eL5mN9pK2vB8cWuO4jR6tY0sD3xZ7aE2fG5hI8jK1lM4n', 'admin2@aerolinea.com', 'ADMIN', 'ACTIVO'),
                                                                                           (14, 'Rodrigo', '$2a$05$c0tBZNkuiYDjk5.LYK.5quZtCfk8pmdwFCX99xHBwLUDEUmen8G6u', 'Rodle777@gmail.com', 'ADMIN', 'ACTIVO'),
                                                                                           (15, 'Leandro', '$2a$05$vLOk/wiSEJPSDG2wDZOGUeFdi8w2kna756BRgfiXRhpSwFY/Ibmh6', 'Leandro777@gmail.com', 'CLIENTE', 'ACTIVO');

-- --------------------------------------------------------

--
-- Table structure for table `vuelo`
--

DROP TABLE IF EXISTS `vuelo`;
CREATE TABLE IF NOT EXISTS `vuelo` (
                                       `id_vuelo` bigint NOT NULL AUTO_INCREMENT,
                                       `id_ruta` bigint DEFAULT NULL,
                                       `id_avion` bigint DEFAULT NULL,
                                       `fecha_salida` datetime(6) NOT NULL,
                                       `fecha_llegada` datetime(6) NOT NULL,
                                       `estado` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                       `nombre_piloto` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `nombre_copiloto` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                       PRIMARY KEY (`id_vuelo`),
                                       KEY `fk_vuelo_ruta` (`id_ruta`),
                                       KEY `fk_vuelo_avion` (`id_avion`),
                                       KEY `idx_vuelo_estado` (`estado`),
                                       KEY `idx_vuelo_fecha_salida` (`fecha_salida`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `vuelo`
--

INSERT INTO `vuelo` (`id_vuelo`, `id_ruta`, `id_avion`, `fecha_salida`, `fecha_llegada`, `estado`, `nombre_piloto`, `nombre_copiloto`) VALUES
                                                                                                                                           (1, 1, 1, '2026-05-20 06:00:00.000000', '2026-05-20 06:45:00.000000', 'PROGRAMADO', 'Cap. Roberto Funes', 'First Of. Daniel Guevara'),
                                                                                                                                           (2, 2, 2, '2026-05-20 08:00:00.000000', '2026-05-20 09:30:00.000000', 'PROGRAMADO', 'Cap. Antonio Serrano', 'First Of. Luis Paredes'),
                                                                                                                                           (3, 3, 3, '2026-05-20 10:00:00.000000', '2026-05-20 12:15:00.000000', 'PROGRAMADO', 'Cap. Marcos Aldana', 'First Of. Carlos Rivas'),
                                                                                                                                           (4, 4, 5, '2026-05-21 07:00:00.000000', '2026-05-21 09:30:00.000000', 'PROGRAMADO', 'Cap. Fernando Villanueva', 'First Of. Oscar Navarro'),
                                                                                                                                           (5, 5, 7, '2026-05-21 14:00:00.000000', '2026-05-21 16:45:00.000000', 'PROGRAMADO', 'Cap. Ricardo Mendoza', 'First Of. Pablo Cruz'),
                                                                                                                                           (6, 6, 4, '2026-05-22 09:00:00.000000', '2026-05-22 10:50:00.000000', 'PROGRAMADO', 'Cap. Esteban Palacios', 'First Of. Hugo Monterrosa'),
                                                                                                                                           (7, 7, 6, '2026-05-22 11:00:00.000000', '2026-05-22 12:20:00.000000', 'PROGRAMADO', 'Cap. Andrés Cáceres', 'First Of. Mario Peña'),
                                                                                                                                           (8, 8, 3, '2026-05-23 08:00:00.000000', '2026-05-23 09:45:00.000000', 'PROGRAMADO', 'Cap. Jorge Batres', 'First Of. Nelson Raudales'),
                                                                                                                                           (9, 9, 9, '2026-05-23 13:00:00.000000', '2026-05-23 16:30:00.000000', 'PROGRAMADO', 'Cap. Alejandro Ruiz', 'First Of. Tomás Herrera'),
                                                                                                                                           (10, 10, 10, '2026-05-24 10:00:00.000000', '2026-05-24 13:45:00.000000', 'PROGRAMADO', 'Cap. Eduardo Vargas', 'First Of. Francisco Lima'),
                                                                                                                                           (11, 11, 11, '2026-05-24 15:00:00.000000', '2026-05-24 18:00:00.000000', 'PROGRAMADO', 'Cap. Miguel Torres', 'First Of. Andrés Rojas'),
                                                                                                                                           (12, 12, 12, '2026-05-25 07:00:00.000000', '2026-05-25 10:30:00.000000', 'PROGRAMADO', 'Cap. Raúl Castillo', 'First Of. Diego Sánchez'),
                                                                                                                                           (13, 13, 13, '2026-05-25 12:00:00.000000', '2026-05-25 17:30:00.000000', 'PROGRAMADO', 'Cap. William Carter', 'First Of. James Wilson'),
                                                                                                                                           (14, 14, 1, '2026-05-26 06:30:00.000000', '2026-05-26 07:25:00.000000', 'PROGRAMADO', 'Cap. Roberto Funes', 'First Of. Daniel Guevara'),
                                                                                                                                           (15, 15, 8, '2026-05-26 09:00:00.000000', '2026-05-26 10:10:00.000000', 'PROGRAMADO', 'Cap. Héctor Pineda', 'First Of. César Bonilla'),
                                                                                                                                           (16, 16, 6, '2026-05-27 11:00:00.000000', '2026-05-27 12:15:00.000000', 'PROGRAMADO', 'Cap. Andrés Cáceres', 'First Of. Mario Peña'),
                                                                                                                                           (17, 17, 10, '2026-05-27 14:00:00.000000', '2026-05-27 15:50:00.000000', 'PROGRAMADO', 'Cap. Eduardo Vargas', 'First Of. Francisco Lima'),
                                                                                                                                           (18, 18, 4, '2026-05-28 08:00:00.000000', '2026-05-28 11:00:00.000000', 'PROGRAMADO', 'Cap. Esteban Palacios', 'First Of. Hugo Monterrosa'),
                                                                                                                                           (19, 19, 11, '2026-05-28 16:00:00.000000', '2026-05-28 19:40:00.000000', 'PROGRAMADO', 'Cap. Miguel Torres', 'First Of. Andrés Rojas'),
                                                                                                                                           (20, 20, 12, '2026-05-29 09:00:00.000000', '2026-05-29 16:00:00.000000', 'PROGRAMADO', 'Cap. Raúl Castillo', 'First Of. Diego Sánchez'),
                                                                                                                                           (21, 1, 14, '2026-05-20 14:00:00.000000', '2026-05-20 14:45:00.000000', 'RETRASADO', 'Cap. Manuel Zepeda', 'First Of. Ricardo Orellana'),
                                                                                                                                           (22, 2, 2, '2026-05-21 16:00:00.000000', '2026-05-21 17:30:00.000000', 'RETRASADO', 'Cap. Antonio Serrano', 'First Of. Luis Paredes'),
                                                                                                                                           (23, 5, 7, '2026-05-22 18:00:00.000000', '2026-05-22 20:45:00.000000', 'RETRASADO', 'Cap. Ricardo Mendoza', 'First Of. Pablo Cruz'),
                                                                                                                                           (24, 3, 3, '2026-05-23 20:00:00.000000', '2026-05-23 22:15:00.000000', 'CANCELADO', 'Cap. Marcos Aldana', 'First Of. Carlos Rivas'),
                                                                                                                                           (25, 9, 9, '2026-05-24 22:00:00.000000', '2026-05-25 01:30:00.000000', 'CANCELADO', 'Cap. Alejandro Ruiz', 'First Of. Tomás Herrera');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `avion`
--
ALTER TABLE `avion`
    ADD CONSTRAINT `fk_avion_aerolinea` FOREIGN KEY (`id_aerolinea`) REFERENCES `aerolinea` (`id_aerolinea`);

--
-- Constraints for table `pago`
--
ALTER TABLE `pago`
    ADD CONSTRAINT `fk_pago_reservacion` FOREIGN KEY (`id_reservacion`) REFERENCES `reservacion` (`id_reservacion`);

--
-- Constraints for table `pasajero`
--
ALTER TABLE `pasajero`
    ADD CONSTRAINT `fk_pasajero_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`);

--
-- Constraints for table `reservacion`
--
ALTER TABLE `reservacion`
    ADD CONSTRAINT `fk_reservacion_pasajero` FOREIGN KEY (`id_pasajero`) REFERENCES `pasajero` (`id_pasajero`),
  ADD CONSTRAINT `fk_reservacion_vuelo` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelo` (`id_vuelo`);

--
-- Constraints for table `ruta`
--
ALTER TABLE `ruta`
    ADD CONSTRAINT `fk_ruta_destino` FOREIGN KEY (`id_aeropuerto_destino`) REFERENCES `aeropuerto` (`id_aeropuerto`),
  ADD CONSTRAINT `fk_ruta_origen` FOREIGN KEY (`id_aeropuerto_origen`) REFERENCES `aeropuerto` (`id_aeropuerto`);

--
-- Constraints for table `tarifa`
--
ALTER TABLE `tarifa`
    ADD CONSTRAINT `fk_tarifa_vuelo` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelo` (`id_vuelo`);

--
-- Constraints for table `vuelo`
--
ALTER TABLE `vuelo`
    ADD CONSTRAINT `fk_vuelo_avion` FOREIGN KEY (`id_avion`) REFERENCES `avion` (`id_avion`),
  ADD CONSTRAINT `fk_vuelo_ruta` FOREIGN KEY (`id_ruta`) REFERENCES `ruta` (`id_ruta`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
