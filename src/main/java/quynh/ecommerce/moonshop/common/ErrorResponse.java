package quynh.ecommerce.moonshop.common;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private Object details;
    private Instant timestamp;
    private String path;
}
