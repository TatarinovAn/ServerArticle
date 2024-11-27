package meteor.serverarticle.auth;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import meteor.serverarticle.service.DetailsServiceImpl;
import meteor.serverarticle.service.JwtService;
import meteor.serverarticle.utils.UsernamePasswordAuthentication;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static meteor.serverarticle.utils.Const.*;


@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtService jwtService;


    private final DetailsServiceImpl detailsServiceImpl;


//фильтр авторизации

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        //записываем токен
        String authorizationKey = request.getHeader("Authorization");
        if(Optional.ofNullable(authorizationKey).isPresent() && authorizationKey.startsWith(BEARER)) {
            authorizationKey = authorizationKey.replace(BEARER, "").trim();
            //System.out.println(authorizationKey + "JWT Filter it's");
            try {
                if (jwtService.isValidJwt(authorizationKey)) {
                    //System.out.println("JWT Valid");
                    var claims = jwtService.getClaims(authorizationKey);
                    var username = String.valueOf(claims.get(USERNAME));


                    var authorities = detailsServiceImpl.loadUserByUsername(username).getAuthorities();

                    //System.out.println(authorities + "моя роль тут");
                    Authentication authentication = new UsernamePasswordAuthentication(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("For " + username + " successfully passed validation");
                }
            } catch (JwtException e) {
                SecurityContextHolder.getContext().setAuthentication(null);
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                logger.error("ERROR for this token");
            }
        }
        filterChain.doFilter(request, response);
    }
//
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //System.out.println("shouldNotFilter in JwtAuthorizationFilter" + request.getServletPath());
        return request.getServletPath().equals("/login");
    }

}
