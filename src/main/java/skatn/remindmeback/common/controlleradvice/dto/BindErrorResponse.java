package skatn.remindmeback.common.controlleradvice.dto;

import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import skatn.remindmeback.common.exception.ErrorCode;

import java.util.List;

@Getter
public class BindErrorResponse extends ErrorResponse {

    private final List<FieldError> fieldErrors;
    private final List<String> globalErrors;

    public BindErrorResponse(ErrorCode errorCode, BindingResult bindingResult) {
        super(errorCode);
        this.fieldErrors = bindingResult.getFieldErrors().stream()
                .map(e -> new FieldError(e.getField(), e.getRejectedValue(), e.getDefaultMessage()))
                .toList();

        this.globalErrors = bindingResult.getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}
