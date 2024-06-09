package skatn.remindmeback.common.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import skatn.remindmeback.common.controlleradvice.dto.BindErrorResponse;
import skatn.remindmeback.common.controlleradvice.dto.ErrorResponse;
import skatn.remindmeback.common.exception.BaseException;
import skatn.remindmeback.common.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestControllerExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<BindErrorResponse> handleBindException(BindException e) {
        return new ResponseEntity<>(
                new BindErrorResponse(ErrorCode.BAD_REQUEST, e.getBindingResult()),
                HttpStatus.valueOf(ErrorCode.BAD_REQUEST.getStatus())
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        return errorCodeToErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return errorCodeToErrorResponse(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAAccessDeniedException(AccessDeniedException e) {
        return errorCodeToErrorResponse(ErrorCode.PERMISSION_DENIED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception e) {
        return new HashMap<>(){{
            put("message", "Server error");
        }};
    }


    private ResponseEntity<ErrorResponse> errorCodeToErrorResponse(ErrorCode errorCode) {
        return new ResponseEntity<>(
                new ErrorResponse(errorCode),
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }


}
