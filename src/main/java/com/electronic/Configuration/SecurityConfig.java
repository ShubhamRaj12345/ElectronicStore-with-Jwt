package com.electronic.Configuration;
import com.electronic.Security.JwtAuthenticationEntryPoint;
import com.electronic.Security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled =true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
        http
                .csrf(csrf -> csrf.disable()); // isko add kiya tab post create user chala

//        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
//        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        // added global cors
        http.cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();
                                // origins
                                // methods
//                                corsConfiguration.addAllowedOrigin("http://localhost:5173");

                                // for multiple origins
//                                corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                                corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
                                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                                corsConfiguration.setAllowedHeaders(List.of("*"));
//                                corsConfiguration.setAllowedHeaders(List.of("Authentication"));
                                corsConfiguration.setMaxAge(3600L);

                                return corsConfiguration;
                            }
                        })

                );

        http.authorizeHttpRequests(request-> {
            request  .requestMatchers(HttpMethod.DELETE,"/user/delete/**").hasRole("ADMIN")
                               .requestMatchers(HttpMethod.PUT,"/user/**").hasAnyRole("ADMIN","NORMAL")
                               .requestMatchers(HttpMethod.GET ,"/user/**").permitAll()
                               .requestMatchers(HttpMethod.POST ,"/user/**").permitAll()
                               .requestMatchers(HttpMethod.GET,"/product/**").permitAll()
                               .requestMatchers("/product/**").hasRole("ADMIN")
                               .requestMatchers(HttpMethod.GET,"/category/**").permitAll()
                               .requestMatchers("/category/**").hasRole("ADMIN")
                               .requestMatchers(HttpMethod.POST,"/auth/generate-token").permitAll()
                               .requestMatchers("/auth/**").authenticated()
                               .anyRequest().permitAll();
        });
        // ab mai basic security use nahi karenge ab jwt use hoga
             //  http.httpBasic(Customizer.withDefaults());

        // entry point create kiya hai
        http.exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));


        //ye mai sever par kush nahi rakhna chata is liye ye use kiya hai
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // ab filter configure kiya hai
        http.addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);
               return http.build(); // .build kar dega return securityFilter ka implementation class ka object
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
