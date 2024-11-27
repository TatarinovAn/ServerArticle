package meteor.serverarticle.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meteor.serverarticle.dto.LoginRequest;


import meteor.serverarticle.dto.LoginResponse;
import meteor.serverarticle.repository.TokenRepository;
import meteor.serverarticle.service.ArticleService;
import meteor.serverarticle.service.JwtService;
import meteor.serverarticle.utils.UsernamePasswordAuthentication;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

import static meteor.serverarticle.utils.Const.AUTH_TOKEN_KEY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsernamePasswordAuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;


    Logger logger = LoggerFactory.getLogger(InitialAuthenticationFilter.class);
    //фильтр аутентификации
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {


//если нет токена в загаловке
        if (request.getHeader(AUTH_TOKEN_KEY) == null) {
            String bodyJson = request.getReader().readLine();

            if (bodyJson != null) {
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(bodyJson, LoginRequest.class);

                String username = loginRequest.getUsername();
                String password = loginRequest.getPassword();

                try {

                    //сверяем пароль и логин с бд
                    Authentication authentication = new UsernamePasswordAuthentication(username, password, null);

                    authentication = authenticationProvider.authenticate(authentication);

                    String jwt = jwtService.generatedJwt(authentication);

                    tokenRepository.putAuthToken(username, jwt);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);

                    //записываем токен в заголовок
                    response.getWriter().write(mapper.writeValueAsString(new LoginResponse(jwt)));
                    response.getWriter().flush();
                    logger.info("For " + username + " successfully created token");
                } catch (BadCredentialsException | ObjectNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    logger.error("For " + username + " ERROR created token");
                }
            }
        }
    }

    //не фильтруем если не /login
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        System.out.println("shouldNotFilter in InitialAuthenticationFilter" + request.getServletPath());
        return !request.getServletPath().equals("/login");
    }
}
