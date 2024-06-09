package skatn.remindmeback.common.exception;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
