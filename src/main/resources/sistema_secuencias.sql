
CREATE TABLE `sistema_secuencias` (
  `ID_SECUENCIA` int(11) NOT NULL,
  `TABLA` varchar(20) NOT NULL,
  `SECUENCIA` int(11) NOT NULL,
  `SISTEMA` tinyint(4) DEFAULT '1'
);

--

--
-- Indices de la tabla `sistema_secuencias`
--
ALTER TABLE `sistema_secuencias`
  ADD PRIMARY KEY (`ID_SECUENCIA`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `sistema_secuencias`
--
ALTER TABLE `sistema_secuencias`
  MODIFY `ID_SECUENCIA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
