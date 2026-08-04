package quynh.ecommerce.moonshop.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import quynh.ecommerce.moonshop.common.ErrorResponse;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException e, HttpServletRequest request){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .details(null)
                        .timestamp(Instant.now())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                   HttpServletRequest request) {
        Map<String, String> details = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage(),
                        (first, second) -> first
                ));

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code(ErrorCode.VALIDATION_ERROR.name())
                .message(ErrorCode.VALIDATION_ERROR.getMessage())
                .details(details)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                                                                     HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.builder()
                .code(ErrorCode.FORBIDDEN.name())
                .message(ErrorCode.FORBIDDEN.getMessage())
                .details(null)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public  ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .code(ErrorCode.USER_NOT_FOUND.name())
                .message(ex.getMessage())
                .details(null)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public  ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request){
        log.error("Unexpected server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .details(null)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build());
    }
}
