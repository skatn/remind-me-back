package skatn.remindmeback.common.controlleradvice.dto;

import lombok.Getter;
import skatn.remindmeback.common.exception.ErrorCode;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
