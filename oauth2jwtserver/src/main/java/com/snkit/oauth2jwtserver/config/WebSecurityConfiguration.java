package com.snkit.oauth2jwtserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) 
      throws Exception {
		 auth.inMemoryAuthentication().withUser("sairam").password("{bcrypt}$2a$10$qGArWXcn2gvK75Ub3Gukm.JE4xuunC4wpEt5Y72sXdcW3rt2ZJnFi").authorities("ROLE_REPORTS")
		    .and().withUser("ramsai").password("{bcrypt}$2a$10$qGArWXcn2gvK75Ub3Gukm.JE4xuunC4wpEt5Y72sXdcW3rt2ZJnFi").authorities("ROLE_ADMIN");
    }
 
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() 
      throws Exception {
        return super.authenticationManagerBean();
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll();
    }
}
