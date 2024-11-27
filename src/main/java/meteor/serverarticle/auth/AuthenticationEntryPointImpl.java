//package meteor.serverarticle.auth;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import meteor.serverarticle.dto.ErrorResponse;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//
//import java.io.IOException;
//
//@Component
//public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        var mapper = new ObjectMapper();
//        //задаем контент
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        //статус 401
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        response.getWriter().write(mapper.writeValueAsString(
//                new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Error auth")));
//        response.getWriter().flush();
//    }
//}
