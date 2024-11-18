package proyecto.cocinasegura.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration


public class SecurityConfig {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                // Rutas públicas
                                                .requestMatchers("/", "/login", "/buscar", "/assets/**", "/css/**",
                                                                "/js/**", "/images/**", "/webjars/**")
                                                .permitAll()
                                                // Rutas protegidas
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/perform_login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .permitAll())
                                .authenticationProvider(authenticationProvider());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());

                return authProvider;
        }

        @Bean
        public CookieHttpSessionIdResolver cookieHttpSessionIdResolver() {
                CookieHttpSessionIdResolver resolver = new CookieHttpSessionIdResolver();
                DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
                cookieSerializer.setSameSite("None");
                cookieSerializer.setUseSecureCookie(true);
                resolver.setCookieSerializer(cookieSerializer);
                return resolver;
        }

}