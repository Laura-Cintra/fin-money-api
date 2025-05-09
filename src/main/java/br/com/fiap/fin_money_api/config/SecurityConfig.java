package br.com.fiap.fin_money_api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private AuthFilter authFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(
            auth -> auth
                //.requestMatchers("/categories/**").hasRole("ADMIN")
                .requestMatchers("/login/**").permitAll()
                .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // o filtro que vou adicionar, antes de quem
        .httpBasic(Customizer.withDefaults())
        .build();
    }

    // Encontrando usuário no armazenamento local
    // @Bean
    // UserDetailsService userDetailsService(){
        
    //     var users = List.of(
    //         User
    //             .withUsername("laura")
    //             .password("$2a$12$rAAfVcGyEr6urvnNby1ke.Z917LH7iBzoSqtdD5ruC.TPdNiMoLPa")
    //             .roles("ADMIN")
    //             .build(),
    //         User
    //             .withUsername("maria")
    //             .password("$2a$12$rCk9GJa9xEc3XNRaJwNLZ.ikrQU7he9hxNHTJmiYh9ij58PXuclW2")
    //             .roles("USER")
    //             .build()
    //     );

    //     return new InMemoryUserDetailsManager(users); // usuário está em memória e não no BD
    // }
    
    // sempre que eu pedir "passwordEncoder" usará o BCrypt
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // gera a senha criptografada
    }

    // informando ao spring de onde ele vai tirar o AuthenticationManager
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
