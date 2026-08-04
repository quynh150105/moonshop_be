package quynh.ecommerce.moonshop.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import quynh.ecommerce.moonshop.common.ErrorResponse;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.UNAUTHORIZED.name())
                .message(ErrorCode.UNAUTHORIZED.getMessage())
                .details(null)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        objectMapper.writeValue(response.getOutputStream(), errorResponse);

    }
}
