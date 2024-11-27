package meteor.serverarticle.auth;

import lombok.RequiredArgsConstructor;
import meteor.serverarticle.service.DetailsServiceImpl;
import meteor.serverarticle.utils.UserDetailsIml;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
//сверка пароля с базой данных
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider {
    private final DetailsServiceImpl detailsServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public Authentication authenticate (Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());


        UserDetailsIml userDetails =  (UserDetailsIml) detailsServiceImpl.loadUserByUsername(username);
        //System.out.println(userDetails.getPassword());
       // System.out.println(password);
        if(passwordEncoder.matches(password, passwordEncoder.encode(userDetails.getPassword()))) {
           // System.out.println("USERNAME_PASSWORD_AUTH " + userDetails.getAuthorities());
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());


        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
