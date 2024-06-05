package skatn.remindmeback.common.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import skatn.remindmeback.common.controlleradvice.dto.BindErrorResponse;
import skatn.remindmeback.common.exception.ErrorCode;

@RestControllerAdvice
public class GlobalRestControllerExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<BindErrorResponse> handleBindException(BindException e) {
        return new ResponseEntity<>(new BindErrorResponse(ErrorCode.INVALID_REQUEST_PARAMETER, e.getBindingResult()),
                HttpStatus.valueOf(ErrorCode.INVALID_REQUEST_PARAMETER.getStatus()));
    }


}
