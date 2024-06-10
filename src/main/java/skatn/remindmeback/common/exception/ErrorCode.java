package skatn.remindmeback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Bind
    BAD_REQUEST(400, "REQ_001", "요청이 잘못 되었습니다."),

    // Auth
    ALREADY_USED_USERNAME(400, "AUTH_001", "이미 사용 중인 아이디 입니다."),
    PERMISSION_DENIED(403, "AUTH_002", "권한이 없습니다."),
    INVALID_USERNAME_OR_PASSWORD(401, "AUTH_003", "아이디 또는 비밀번호가 일치 하지 않습니다."),
    JWT_NO_AUTHORITIES_KEY(401, "AUTH_004", "JWT에 권한 정보가 없습니다."),
    JWT_INVALID_SIGNATURE(401, "AUTH_005", "잘못된 JWT 서명입니다."),
    JWT_EXPIRED(401, "AUTH_006", "만료된 JWT 입니다."),
    JWT_UNSUPPORTED(401, "AUTH_007", "지원되지 않는 JWT 입니다."),
    JWT_INVALID(401, "AUTH_008", "JWT가 잘못되었습니다."),
    JWT_NO_AUTHENTICATION_INFO(401, "AUTH_009", "인증정보가 없습니다."),
    JWT_INVALID_REFRESH_TOKEN(401, "AUTH_010", "유효하지 않은 리프레시 토큰입니다."),
    UN_AUTHORIZE(401, "AUTH_011", "인증 되지 않았습니다."),
    INVALID_OAUTH_PROVIDER(400, "AUTH_012", "유효하지 않은 OAuth2 서비스 입니다."),

    // Member
    MEMBER_NOT_FOUND(404, "MEMBER_001", "멤버를 찾을 수 없습니다."),

    // Subject
    SUBJECT_NOT_FOUND(404, "SUB_001", "문제집을 찾을 수 없습니다."),

    // Question
    QUESTION_NOT_FOUND(404, "Q_001", "문제를 찾을 수 없습니다."),



    ;

    private final int status;
    private final String code;
    private final String message;
}
