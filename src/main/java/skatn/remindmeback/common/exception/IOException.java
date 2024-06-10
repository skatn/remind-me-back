package skatn.remindmeback.common.exception;

public class IOException extends BaseException {
    public IOException(ErrorCode errorCode) {
        super(errorCode);
    }

    public IOException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
