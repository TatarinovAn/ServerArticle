package meteor.serverarticle.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import meteor.serverarticle.repository.TokenRepository;
import meteor.serverarticle.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static meteor.serverarticle.utils.Const.*;

@RequiredArgsConstructor
@Component
public class CustomLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler implements LogoutSuccessHandler {
    JwtService jwtService;
    TokenRepository tokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
var authorizationKey = request.getHeader(AUTH_TOKEN_KEY);
//Optional - класс обёртка для удобства обработки null значений java 8


        // если ключ есть и начинается на BEARER
if(Optional.ofNullable(authorizationKey).isPresent() && authorizationKey.startsWith(BEARER)) {
    // удаляем BEARER и пробел
    authorizationKey = authorizationKey.replace(BEARER, "").trim();

    //получаем набор информации о пользователи с текущем токеном
    Claims claim = jwtService.getClaims(authorizationKey);
   // System.out.println("claim onLogoutSuccess" + claim.toString());
    // и получаем от туда имя пользователя
    var username = String.valueOf(claim.get(USERNAME));

    // удаляем токен этого пользователя
    tokenRepository.removeAuthTokenByUsername(username);
}
// ответ 200 ок
response.setStatus(HttpServletResponse.SC_OK);

// передаем в класс потомок
        super.onLogoutSuccess(request, response, authentication);
    }
}
// выход осуществлен
