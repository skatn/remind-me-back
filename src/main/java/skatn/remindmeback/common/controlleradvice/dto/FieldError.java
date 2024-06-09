package skatn.remindmeback.common.controlleradvice.dto;

import lombok.Getter;

@Getter
public class FieldError {
    private final String field;
    private final Object rejectedValue;
    private final String message;

    public FieldError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
