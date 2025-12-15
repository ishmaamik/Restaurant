package Security.configuration;

import Security.jwt.JWTService;
import Security.jwt.JwtAuthenticationFilter;
import Security.services.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)    //enforces method level security
public class SecurityConfig {
    private final JWTService jwtService;
    private final AppUserDetailsService appUserDetailsService;

    public SecurityConfig(JWTService jwtService, AppUserDetailsService appUserDetailsService){
        this.appUserDetailsService= appUserDetailsService;
        this.jwtService= jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider(appUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg){
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        JwtAuthenticationFilter jwtAuthenticationFilter= new JwtAuthenticationFilter(jwtService);

        http
                .csrf(csrf-> csrf.disable())
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   //Enforces JWT all the time instead of storing auth info everytime
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/waiter/**").hasAnyRole("WAITER", "ADMIN")
                        .requestMatchers("/api/cook/**").hasAnyRole("COOK", "ADMIN")
                        .requestMatchers("/api/cashier/**").hasAnyRole("CASHIER", "ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
