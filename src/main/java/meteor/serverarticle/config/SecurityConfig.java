package meteor.serverarticle.config;

import lombok.RequiredArgsConstructor;
//import meteor.serverarticle.auth.AuthenticationEntryPointImpl;
import meteor.serverarticle.auth.CustomLogoutSuccessHandler;
import meteor.serverarticle.auth.InitialAuthenticationFilter;
import meteor.serverarticle.auth.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@RequiredArgsConstructor
@EnableWebSecurity
@EnableWebMvc
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Value("${cors.credentials}")
    private Boolean credentials;

    @Value("${cors.origins}")
    private String origins;

    @Value("${cors.methods}")
    private String methods;

    @Value("${cors.headers}")
    private String headers;

    //private final AuthenticationEntryPointImpl authenticationEntryPointImp;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    //private final AuthenticationEntryPointImpl authenticationEntryPointImp;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(credentials);
        configuration.setAllowedOrigins(List.of(origins));
        configuration.setAllowedMethods(List.of(methods));
        configuration.setAllowedHeaders(List.of(headers));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8180")
                .allowedMethods("*");
    }


// настройка выхода с удалением токена
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, InitialAuthenticationFilter initialAuthenticationFilter, JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {

//добавлям свой фильтр запросов и фильтр авторизации
        http
                .addFilterAt(initialAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAt(jwtAuthorizationFilter, BasicAuthenticationFilter.class);


        //если страничка логин то пускаем
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated());
//исключение
// AuthenticationEntryPoint — точка входа, используется в случае,
// если в процессе аутентификации возникла ошибка
// и требуется снова запросить у пользователя данные для аутентификации

//        http.exceptionHandling(ex -> ex
//                .authenticationEntryPoint(authenticationEntryPointImp));


//если logout то выходим и удаляем авторизацию
        http.logout(logout -> logout
                        .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler()));

// отключае корс
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
