package gymRestService;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/*
 * Clase que representa un mapeador de ejercicios. Obtiene y crea un 
 * ejercicio a partir de los datos obtenidos de la base de datos.
 * Este RowMapper es para obtener los datos de la tablas 'ejercicios' y 'sesiones'
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class EjercicioBRowMapper implements RowMapper<EjercicioB> {

    public EjercicioB mapRow(ResultSet resultSet, int arg1) throws SQLException {
    	// getX(index) para coger los elementos recibidos por la tabla de datos
    	EjercicioB ejercicioB = new EjercicioB(resultSet.getInt(1), resultSet.getString(2),
		resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));

	return ejercicioB;
    }
}
