package gymRestService.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/*
 * Clase que hospeda los recursos (nuestra api rest) en los 
 * que el cliente está interesado
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@Configuration
@EnableResourceServer //Activa un filtro de Spring Security que autentifica peticiones usando un token Oauth2 entrante
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "rest-service-monitor";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/gymRegister/**").permitAll(); //Permitimos el acceso para poder registrarse
		http.
		anonymous().disable()
		.requestMatchers().antMatchers("/gym/**") //Resto de recursos del servidor necesitan autorización para acceder
		.and().authorizeRequests()
		.antMatchers("/gym/**").access("hasRole('USER')")
		.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}

}
