SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";



CREATE DATABASE IF NOT EXISTS `datosgym` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `datosgym`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `datosgym`
--

-- --------------------------------------------------------

--


-- Estructura de tabla para la tabla `caracteristicasdeejercicios`
--

CREATE TABLE IF NOT EXISTS `caracteristicasdeejercicios` (
`Id_Caracteristica` int(11) NOT NULL,
  `Id_Ejercicio` int(11) NOT NULL,
  `descripcion` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ejercicios`
--

CREATE TABLE IF NOT EXISTS `ejercicios` (
`Id_Ejercicio` int(11) NOT NULL,
  `nombre_Ejercicio` varchar(45) NOT NULL,
  `Descripcion` varchar(120) NOT NULL,
  `Finalidad` varchar(45) NOT NULL,
  `Duracion` varchar(45) NOT NULL,
  `URL_video` varchar(80) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Volcado de datos para la tabla `ejercicios`
--

INSERT INTO `ejercicios` (`Id_Ejercicio`, `nombre_Ejercicio`, `Descripcion`, `Finalidad`, `Duracion`, `URL_video`) VALUES
(1, 'Abdominales bicicleta', 'Debemos tumbarnos en el suelo con piernas extendidas y los pies ligeramente levantados', 'Finalidad1', '15min', '0V6F4FNznAA'),
(2, 'Elevaciones de piernas', 'Descripcion2', 'Finalidad2', 'Duracion2', 'Sfhiur099Bc'),
(3, 'Abdominales en V', 'Descripcion3', 'finalidad3', 'Duracion3', 'f3r-gZlPNx8'),
(4, 'Flexiones', 'descripcion4', 'finalidad4', '14', 'meztluvajRY'),
(7, 'Sentadillas', 'descripcion7', 'finalidad7', 'duracion7', '8EFj0dCfY9s');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ejerciciosdesesion`
--

CREATE TABLE IF NOT EXISTS `ejerciciosdesesion` (
  `Id_Ejercicio` int(11) NOT NULL,
  `Id_Sesion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `ejerciciosdesesion`
--

INSERT INTO `ejerciciosdesesion` (`Id_Ejercicio`, `Id_Sesion`) VALUES
(1, 19),
(3, 19),
(3, 22);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `explicacionesdeejercicios`
--

CREATE TABLE IF NOT EXISTS `explicacionesdeejercicios` (
`Id_Explicacion` int(11) NOT NULL,
  `Id_Ejercicio` int(11) NOT NULL,
  `descripcion` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `musculos`
--

CREATE TABLE IF NOT EXISTS `musculos` (
`Id_Musculo` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `url_Foto` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `musculosdeejercicios`
--

CREATE TABLE IF NOT EXISTS `musculosdeejercicios` (
  `Id_Ejercicio` int(11) NOT NULL,
  `Id_Musculo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recomendacionesdeejercicios`
--

CREATE TABLE IF NOT EXISTS `recomendacionesdeejercicios` (
`Id_Recomendacion` int(11) NOT NULL,
  `Id_Ejercicio` int(11) NOT NULL,
  `descripcion` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recursos`
--

CREATE TABLE IF NOT EXISTS `recursos` (
`Id_Recurso` int(11) NOT NULL,
  `titulo` varchar(45) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `posicion_foto` varchar(20) NOT NULL,
  `url` varchar(150) NOT NULL,
  `tipo_recurso` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recursosdeejercicios`
--

CREATE TABLE IF NOT EXISTS `recursosdeejercicios` (
  `Id_Ejercicio` int(11) NOT NULL,
  `Id_Recurso` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sesiones`
--

CREATE TABLE IF NOT EXISTS `sesiones` (
`Id_Sesion` int(11) NOT NULL,
  `user_Monitor` varchar(45) NOT NULL,
  `nombre_Sesion` varchar(45) NOT NULL,
  `fecha` varchar(45) NOT NULL,
  `num_Usuarios` int(11) NOT NULL,
  `ip_Monitor` varchar(20) NOT NULL,
  `activa` tinyint(1) NOT NULL,
  `descripcion` varchar(45) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=26 ;

--
-- Volcado de datos para la tabla `sesiones`
--

INSERT INTO `sesiones` (`Id_Sesion`, `user_Monitor`, `nombre_Sesion`, `fecha`, `num_Usuarios`, `ip_Monitor`, `activa`, `descripcion`) VALUES
(19, 'pablo', 'Entrenamiento abdominales', '2015-05-02 02:00:00', 1, '192.168.1.105:54322', 1, ''),
(21, 'pablo', 'Pecho + espalda', '2015-05-21 01:00:00', 1, '192.168.1.105', 0, ''),
(22, 'pablo', 'Entrenamiento pectorales', '2015-05-13 02:00:00', 0, '192.168.1.105:54321', 1, ''),
(23, 'pablo', 'Sentadillas en media hora', '2015-05-07 00:00:00', 0, '192.168.1.105', 1, ''),
(24, 'pablo', 'Ejercicios tren superior', '2015-05-11 19:00:00', 6, '192.168.1.105:54322', 1, ''),
(25, 'pablo', 'Entrenamiento tren inferior', '2015-06-25 02:00:00', 1, '192.168.1.105:54321', 0, '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sesionesdeusuario`
--

CREATE TABLE IF NOT EXISTS `sesionesdeusuario` (
  `Id_Sesion` int(11) NOT NULL,
  `Id_Usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `sesionesdeusuario`
--

INSERT INTO `sesionesdeusuario` (`Id_Sesion`, `Id_Usuario`) VALUES
(19, 1),
(19, 2),
(22, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE IF NOT EXISTS `usuarios` (


`Id_Usuario` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `apellidos` varchar(30) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `direccion` varchar(40) NOT NULL,
  `movil` varchar(20) NOT NULL,
  `username` varchar(20) NOT NULL,
  `passw` varchar(80) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  UNIQUE KEY `usuario` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`Id_Usuario`, `nombre`, `apellidos`, `dni`, `direccion`, `movil`, `username`, `passw`) VALUES
(1, 'Pablo', 'Gallardo Hiraldo', '50000000F', 'calle', '666666666', 'pablo', '$2a$12$uz1p4hSnwNW2gtCjEP9mL.2jUyRra7phi6xAW8P3Pn44mhTtb.DDK'),
(2, 'Cliente', 'Cliente', '49000000L', 'calle', '691505050', 'cliente', '$2a$12$VL4uNuki57HRZEYeXArkQOlTyrYCqQKF3oE6SIKVNqqFzsPL7c2Sm'),
(3, 'Usuario', 'Usuario', '48000000L', 'calle', '638604940', 'usuario', '$2a$12$7rR9CZC90H61hM6zG5IjWOdIg/W/LG5o14r1QDDJiYdCxB5L/T/ZS'),
(4, 'Consumidor', 'Consumidor', '47000000L', 'calle', '691592550', 'consumidor', '$2a$12$rsm0iczvN20vocSqs/Fe1er4N3y3ut/P7I7k.ly4LcdjtajYGPhb2');


-- --------------------------------------------------------

-- Estructura de tabla para la tabla `roles_usuarios`


CREATE TABLE IF NOT EXISTS `roles_usuarios` (

`user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `role` varchar(30) NOT NULL,
  PRIMARY KEY (user_role_id),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES usuarios (username)ON DELETE CASCADE ON UPDATE CASCADE);

--
-- Volcado de datos para la tabla `roles_usuarios`
--

 INSERT INTO roles_usuarios (username, role)
  VALUES ('pablo', 'ROLE_USER');
  INSERT INTO roles_usuarios (username, role)
  VALUES ('cliente', 'ROLE_USER');
  INSERT INTO roles_usuarios (username, role)
  VALUES ('usuario', 'ROLE_USER');
  INSERT INTO roles_usuarios (username, role)
  VALUES ('consumidor', 'ROLE_USER');


--
-- Estructura de tabla para la tabla `utiles`
--

CREATE TABLE IF NOT EXISTS `utiles` (
`Id_Util` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `url_Foto` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `utilesdeejercicios`
--

CREATE TABLE IF NOT EXISTS `utilesdeejercicios` (
  `Id_Ejercicio` int(11) NOT NULL,
  `Id_Util` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `caracteristicasdeejercicios`
--
ALTER TABLE `caracteristicasdeejercicios`
 ADD PRIMARY KEY (`Id_Caracteristica`,`Id_Ejercicio`), ADD KEY `Id_Ejercicio` (`Id_Ejercicio`);

--
-- Indices de la tabla `ejercicios`
--
ALTER TABLE `ejercicios`
 ADD PRIMARY KEY (`Id_Ejercicio`);

--
-- Indices de la tabla `ejerciciosdesesion`
--
ALTER TABLE `ejerciciosdesesion`
 ADD PRIMARY KEY (`Id_Ejercicio`,`Id_Sesion`), ADD KEY `Id_Sesion` (`Id_Sesion`);

--
-- Indices de la tabla `explicacionesdeejercicios`
--
ALTER TABLE `explicacionesdeejercicios`
 ADD PRIMARY KEY (`Id_Explicacion`,`Id_Ejercicio`), ADD KEY `Id_Ejercicio` (`Id_Ejercicio`);

--
-- Indices de la tabla `musculos`
--
ALTER TABLE `musculos`
 ADD PRIMARY KEY (`Id_Musculo`);

--
-- Indices de la tabla `musculosdeejercicios`
--
ALTER TABLE `musculosdeejercicios`
 ADD PRIMARY KEY (`Id_Ejercicio`,`Id_Musculo`), ADD KEY `Id_Musculo` (`Id_Musculo`);

--
-- Indices de la tabla `recomendacionesdeejercicios`
--
ALTER TABLE `recomendacionesdeejercicios`
 ADD PRIMARY KEY (`Id_Recomendacion`,`Id_Ejercicio`), ADD KEY `Id_Ejercicio` (`Id_Ejercicio`);

--
-- Indices de la tabla `recursos`
--
ALTER TABLE `recursos`
 ADD PRIMARY KEY (`Id_Recurso`);

--
-- Indices de la tabla `recursosdeejercicios`
--
ALTER TABLE `recursosdeejercicios`
 ADD PRIMARY KEY (`Id_Ejercicio`,`Id_Recurso`), ADD KEY `Id_Recurso` (`Id_Recurso`);

--
-- Indices de la tabla `sesiones`
--
ALTER TABLE `sesiones`
 ADD PRIMARY KEY (`Id_Sesion`);

--
-- Indices de la tabla `sesionesdeusuario`
--
ALTER TABLE `sesionesdeusuario`
 ADD PRIMARY KEY (`Id_Sesion`,`Id_Usuario`), ADD KEY `Id_Usuario` (`Id_Usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
 ADD PRIMARY KEY (`Id_Usuario`);

--
-- Indices de la tabla `utiles`
--
ALTER TABLE `utiles`
 ADD PRIMARY KEY (`Id_Util`);

--
-- Indices de la tabla `utilesdeejercicios`
--
ALTER TABLE `utilesdeejercicios`
 ADD PRIMARY KEY (`Id_Ejercicio`,`Id_Util`), ADD KEY `Id_Util` (`Id_Util`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `caracteristicasdeejercicios`
--
ALTER TABLE `caracteristicasdeejercicios`
MODIFY `Id_Caracteristica` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `ejercicios`
--
ALTER TABLE `ejercicios`
MODIFY `Id_Ejercicio` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT de la tabla `explicacionesdeejercicios`
--
ALTER TABLE `explicacionesdeejercicios`
MODIFY `Id_Explicacion` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `musculos`
--
ALTER TABLE `musculos`
MODIFY `Id_Musculo` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `recomendacionesdeejercicios`
--
ALTER TABLE `recomendacionesdeejercicios`
MODIFY `Id_Recomendacion` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `recursos`
--
ALTER TABLE `recursos`
MODIFY `Id_Recurso` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `sesiones`
--
ALTER TABLE `sesiones`
MODIFY `Id_Sesion` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=26;
--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
MODIFY `Id_Usuario` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `utiles`
--
ALTER TABLE `utiles`
MODIFY `Id_Util` int(11) NOT NULL AUTO_INCREMENT;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `caracteristicasdeejercicios`
--
ALTER TABLE `caracteristicasdeejercicios`
ADD CONSTRAINT `caracteristicasdeejercicios_ibfk_1` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ejerciciosdesesion`
--
ALTER TABLE `ejerciciosdesesion`
ADD CONSTRAINT `ejerciciosdesesion_ibfk_1` FOREIGN KEY (`Id_Sesion`) REFERENCES `sesiones` (`Id_Sesion`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `ejerciciosdesesion_ibfk_2` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `explicacionesdeejercicios`
--
ALTER TABLE `explicacionesdeejercicios`
ADD CONSTRAINT `explicacionesdeejercicios_ibfk_1` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `musculosdeejercicios`
--
ALTER TABLE `musculosdeejercicios`
ADD CONSTRAINT `musculosdeejercicios_ibfk_1` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `musculosdeejercicios_ibfk_2` FOREIGN KEY (`Id_Musculo`) REFERENCES `musculos` (`Id_Musculo`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `recomendacionesdeejercicios`
--
ALTER TABLE `recomendacionesdeejercicios`
ADD CONSTRAINT `recomendacionesdeejercicios_ibfk_1` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `recursosdeejercicios`
--
ALTER TABLE `recursosdeejercicios`
ADD CONSTRAINT `recursosdeejercicios_ibfk_1` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `recursosdeejercicios_ibfk_2` FOREIGN KEY (`Id_Recurso`) REFERENCES `recursos` (`Id_Recurso`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `sesionesdeusuario`
--
ALTER TABLE `sesionesdeusuario`
ADD CONSTRAINT `sesionesdeusuario_ibfk_1` FOREIGN KEY (`Id_Sesion`) REFERENCES `sesiones` (`Id_Sesion`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `sesionesdeusuario_ibfk_2` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `utilesdeejercicios`
--
ALTER TABLE `utilesdeejercicios`
ADD CONSTRAINT `utilesdeejercicios_ibfk_1` FOREIGN KEY (`Id_Ejercicio`) REFERENCES `ejercicios` (`Id_Ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `utilesdeejercicios_ibfk_2` FOREIGN KEY (`Id_Util`) REFERENCES `utiles` (`Id_Util`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
