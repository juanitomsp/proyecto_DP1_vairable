package es.us.dp1.lx_xy_24_25.your_game_name.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.us.dp1.lx_xy_24_25.your_game_name.configuration.jwt.AuthEntryPointJwt;
import es.us.dp1.lx_xy_24_25.your_game_name.configuration.jwt.AuthTokenFilter;
import es.us.dp1.lx_xy_24_25.your_game_name.configuration.services.UserDetailsServiceImpl;

import java.util.List;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    DataSource dataSource;

    private static final String ADMIN = "ADMIN";
    private static final String PLAYER = "PLAYER";

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
            .cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(h -> h.frameOptions(f -> f.disable()))
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))

            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                
                // H2 Console
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Páginas públicas
                .requestMatchers("/", "/oups").permitAll()

                // Swagger
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/swagger-resources/**"
                ).permitAll()

                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/developers").permitAll()
                .requestMatchers("/api/v1/plan").permitAll()

                // ACHIEVEMENTS - autenticado
                .requestMatchers(HttpMethod.GET, "/api/v1/achievements").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/achievements/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/achievements/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.PUT, "/api/v1/achievements/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/achievements/**").hasAuthority(ADMIN)

                // USERS - ORDEN CORRECTO
                .requestMatchers(HttpMethod.GET, "/api/v1/users").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/currentUser").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").authenticated()
                
                // STATISTICS
                .requestMatchers("/api/v1/statistics/**").authenticated()

                // GAMES
                .requestMatchers(HttpMethod.POST, "/api/v1/games").hasAuthority(PLAYER)
                .requestMatchers("/api/v1/games/**").permitAll()

                // FRIENDSHIPS - ORDEN CORRECTO: específico antes de general
                // GET endpoints
                .requestMatchers(HttpMethod.GET, "/api/v1/friendships").hasAuthority(PLAYER)
                .requestMatchers(HttpMethod.GET, "/api/v1/friendships/pending").hasAuthority(PLAYER)
                .requestMatchers(HttpMethod.GET, "/api/v1/friendships/**").hasAuthority(PLAYER)
                
                // POST endpoints
                .requestMatchers(HttpMethod.POST, "/api/v1/friendships/send/**").hasAuthority(PLAYER)
                .requestMatchers(HttpMethod.POST, "/api/v1/friendships/**").hasAuthority(PLAYER)
                
                // DELETE endpoints
                .requestMatchers(HttpMethod.DELETE, "/api/v1/friendships/**").hasAuthority(PLAYER)

                // El resto denegado
                .anyRequest().denyAll()
            )

            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}