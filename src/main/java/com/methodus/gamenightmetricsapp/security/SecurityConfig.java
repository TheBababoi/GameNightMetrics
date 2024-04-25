package com.methodus.gamenightmetricsapp.security;

import com.methodus.gamenightmetricsapp.service.PlayerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PlayerService playerService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(playerService); //set the custom user details service
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder()); //set the password encoder  bcrypt
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler
            customAuthenticationSuccessHandler) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/css/**", "/images/**", "/js/**").permitAll());
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/players/showFormForRegister").permitAll()
                                .requestMatchers("/registration-confirmation").permitAll()
                                .requestMatchers("/players/save").permitAll()
                                .requestMatchers("/players/save").permitAll()
                                .requestMatchers("/boardgames/showFormForAdd").hasRole("ADMIN")
                                .requestMatchers("/players/showFormForAdd").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/authenticateThePlayer")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .permitAll()
                                .logoutSuccessUrl("/?logout=true"))
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );


        return http.build();
    }
}
