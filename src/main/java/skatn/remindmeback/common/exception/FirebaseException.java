package skatn.remindmeback.common.exception;

public class FirebaseException extends BaseException {

    public FirebaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FirebaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
