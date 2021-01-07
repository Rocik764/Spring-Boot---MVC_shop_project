package local.umg.susersmvc.config;

import local.umg.susersmvc.details.CustomUserDetailsService;
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

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
				.antMatchers("/admin/listUsers").hasAnyAuthority("ADMIN")
				.antMatchers("/admin/new").hasAnyAuthority("ADMIN")
				.antMatchers("/admin/edit/**").hasAnyAuthority("ADMIN")
				.antMatchers("/admin/delete/**").hasAnyAuthority("ADMIN")
				.antMatchers("/product/new").hasAnyAuthority("ADMIN", "EMPLOYEE")
				.antMatchers("/product/editProduct/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
				.antMatchers("/product/deleteProduct/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
				.antMatchers("/product/newCategory").hasAnyAuthority("ADMIN", "EMPLOYEE")
				.antMatchers("/product/newProducent").hasAnyAuthority("ADMIN", "EMPLOYEE")
				.antMatchers("/app/profile", "/cart/showCart").authenticated()
				.antMatchers("/app/contact").permitAll()
				.antMatchers("/app/register").permitAll()
				.antMatchers("/app/schronisko").permitAll()
				.antMatchers("/app/show_products").permitAll()
				.and()
				.formLogin()
					.loginPage("/app/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.defaultSuccessUrl("/app/")
					.permitAll()
				.and()
				.logout()
					.clearAuthentication(true)
					.deleteCookies("JSESSIONID")
					.invalidateHttpSession(true)
					.logoutSuccessUrl("/app/login")
					.permitAll();
	}


}
