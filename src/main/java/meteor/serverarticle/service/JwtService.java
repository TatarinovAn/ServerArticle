package meteor.serverarticle.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import meteor.serverarticle.entity.User;
import meteor.serverarticle.repository.TokenRepository;
import meteor.serverarticle.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static meteor.serverarticle.utils.Const.*;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${token.signing.key}")
    private String signingKey;

    @Value("${jwt.key.expiration}")
    private Long jwtExpiration;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private SecretKey key;


    //Генерация ключа
    private SecretKey generatedSecretKey() {
        if(key == null) {
            key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        }

        return key;
    }

    public String generatedJwt (Authentication authentication) {
        System.out.println("generatedJwt " + authentication);
        return Jwts.builder()
                .setClaims(
                        Map.of(
                            USERNAME, authentication.getName(),
                                ROLE, authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList()),
                                USER_ID, String.valueOf(userRepository.findByUsername(authentication.getName()).get().getId())))
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .setSubject(authentication.getName())
                .signWith(generatedSecretKey())
                .compact();


    }
    //Утверждений JWT

    public Claims getClaims(String authToken) {

        System.out.println("authToken in getClaims: " + authToken);
        return Jwts.parserBuilder()
                .setSigningKey(generatedSecretKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    public boolean isValidJwt(String authToken) {
        System.out.println("authToken in isValidAuthToken: " + authToken);
        var claims = Jwts.parserBuilder()
                .setSigningKey(generatedSecretKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();

        var username = String.valueOf(claims.get(USERNAME));
       // var user = userRepository.findByUsername(username);
        var tokenFromMemory = tokenRepository.getAuthTokenByUsername(username);

Optional<User> user = userRepository.findByUsername(String.valueOf(claims.get(USERNAME)));
        System.out.println("tokenRepository " + tokenRepository.getAuthTokenByUsername(username));

        return claims.getExpiration().after(new Date())
                && user.isPresent()
                && tokenFromMemory.isPresent()
                && tokenFromMemory.get().equals(authToken);
    }
}
