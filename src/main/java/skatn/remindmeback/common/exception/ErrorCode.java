package skatn.remindmeback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Bind
    INVALID_REQUEST_PARAMETER(400, "REQ_001", "요청 파라미터가 잘못되었습니다."),

    // Auth
    ALREADY_USED_USERNAME(400, "AUTH_001", "이미 사용 중인 아이디 입니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
