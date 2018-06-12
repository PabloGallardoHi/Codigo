package gymRestService;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/*
 * Clase que representa un mapeador de ejercicios. Obtiene y crea un 
 * ejercicio a partir de los datos obtenidos de la base de datos.
 * Este RowMapper es para obtener los datos de la tabla 'ejercicios'
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class EjercicioRowMapper implements RowMapper<Ejercicio> {

    public Ejercicio mapRow(ResultSet resultSet, int arg1) throws SQLException {
    	// getX(index) para coger los elementos recibidos por la tabla de datos
    	Ejercicio ejercicio = new Ejercicio(resultSet.getInt(1), resultSet.getString(2),
		resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
		resultSet.getString(6));

	return ejercicio;
    }
}
