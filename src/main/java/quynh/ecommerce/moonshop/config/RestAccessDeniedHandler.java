package quynh.ecommerce.moonshop.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import quynh.ecommerce.moonshop.common.ErrorResponse;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler  {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.FORBIDDEN.name())
                .message(ErrorCode.FORBIDDEN.getMessage())
                .details(null)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
