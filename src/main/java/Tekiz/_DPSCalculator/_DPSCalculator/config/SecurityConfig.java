package Tekiz._DPSCalculator._DPSCalculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			//controls CSRF protection
			.csrf(AbstractHttpConfigurer::disable
			)
			// configure authorization rules
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest().permitAll()
			)
			//controls the login form
			.formLogin(AbstractHttpConfigurer::disable);
		return http.build();
	}
}
