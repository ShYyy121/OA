package com.example.oa.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    // ... Other configurations

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/user/**").permitAll() // 允许访问登录请求
                .antMatchers("/common/**").permitAll() // 允许访问公共请求
                .antMatchers("/oa/admin/**").hasRole("ADMIN")
                .antMatchers("/lesson/admin/**").hasRole("ADMIN")
                .antMatchers("/oa/teacher/**").hasRole("TEACHER")
                .antMatchers("/lesson/teacher/**").hasRole("TEACHER")
                .antMatchers("/signin/teacher/**").hasRole("TEACHER")
                .antMatchers("/oa/user/**").hasRole("USER")
                .antMatchers("/lesson/user/**").hasRole("USER")
                .antMatchers("/signin/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/user/login").permitAll()
                .and()
                .logout().permitAll();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use a proper password encoder for production use
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}

