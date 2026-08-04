package quynh.ecommerce.moonshop.common.exception;

import lombok.Getter;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
