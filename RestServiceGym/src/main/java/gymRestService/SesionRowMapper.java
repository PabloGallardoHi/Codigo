package gymRestService;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/*
 * Clase que representa un mapeador de sesiones. Obtiene y crea una
 * sesién a partir de los datos obtenidos de la base de datos.
 * Este RowMapper es para obtener los datos de la tabla 'sesiones'
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class SesionRowMapper implements RowMapper<Sesion> {

    public Sesion mapRow(ResultSet resultSet, int arg1) throws SQLException {
    	// getX(index) para coger los elementos recibidos por la tabla de datos
    	Sesion sesion = new Sesion(resultSet.getInt(1), resultSet.getString(2),
		resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5),
		resultSet.getString(6), resultSet.getByte(7), resultSet.getString(8));
	
	return sesion;
    }
}
