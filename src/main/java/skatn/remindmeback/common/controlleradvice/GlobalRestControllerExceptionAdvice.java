package skatn.remindmeback.common.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import skatn.remindmeback.common.controlleradvice.dto.BindErrorResponse;
import skatn.remindmeback.common.controlleradvice.dto.ErrorResponse;
import skatn.remindmeback.common.exception.BaseException;
import skatn.remindmeback.common.exception.ErrorCode;

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
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(
                new ErrorResponse(errorCode),
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.BAD_REQUEST),
                HttpStatus.valueOf(ErrorCode.BAD_REQUEST.getStatus())
        );
    }


}
