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
				.antMatchers(
						"/admin/listUsers",
						"/admin/delete/**",
						"/admin/edit/**",
						"/admin/new").hasAnyAuthority("ADMIN")

				.antMatchers(
						"/product/newCategory",
						"/product/listProducts",
						"/product/newProducent",
						"/product/deleteProduct/**",
						"/product/editProduct/**",
						"/product/new",
						"/ordersRest/showOrdersDetails/{uId}",
						"/admin/showOrders").hasAnyAuthority("ADMIN", "EMPLOYEE")

				.antMatchers(
						"/app/profile",
						"/cart/showCart",
						"/cart/orderProducts",
						"/cart/showOrderDetails",
						"/app/orders").authenticated()

				.antMatchers(
						"/app/contact",
						"/app/register",
						"/app/schronisko",
						"/app/show_products").permitAll()

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
