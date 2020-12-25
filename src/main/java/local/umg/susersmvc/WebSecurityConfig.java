package local.umg.susersmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/listUsers").hasAnyAuthority("ADMIN")
				.antMatchers("/new").hasAnyAuthority("ADMIN")
				.antMatchers("/edit/**").hasAnyAuthority("ADMIN")
				.antMatchers("/delete/**").hasAnyAuthority("ADMIN")
				.antMatchers("/listUsers").hasAnyAuthority("ADMIN")
				.antMatchers("/wlasne").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/profile").authenticated()
				.antMatchers("/contact").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/schronisko").permitAll()
				.antMatchers("/show_products").permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.defaultSuccessUrl("/")
					.permitAll()
				.and()
				.logout()
					.clearAuthentication(true)
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID")
					.invalidateHttpSession(true)
					.permitAll();
	}


}
