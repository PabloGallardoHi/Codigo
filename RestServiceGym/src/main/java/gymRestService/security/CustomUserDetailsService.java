package gymRestService.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gymRestService.UsuarioDaoImpl;

/*
 * Clase que obtiene información sobre un usuario de la base de datos
 * para luego ser usada por ejemplo para la autenticación
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

	// Obtenemos el DAO mediante inyección de dependencias
    @Autowired
    private UsuarioDaoImpl usuarioDao;
	
    // Carga la información de un usuario a través de su username
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
		
		String pass = usuarioDao.getPass(user);
		System.out.println("User : "+user + pass);
		if(user==null){
			System.out.println("User not found");
			throw new UsernameNotFoundException("Username not found"); 
		}
			return new org.springframework.security.core.userdetails.User(user, pass, true, true, true, true, getGrantedAuthorities(user));
	}

	// Carga el rol del usuario
	private List<GrantedAuthority> getGrantedAuthorities(String user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
			String role = usuarioDao.getRole(user);
			authorities.add(new SimpleGrantedAuthority(role));
			
		System.out.print("authorities :"+authorities);
		return authorities;
	}
	
}
