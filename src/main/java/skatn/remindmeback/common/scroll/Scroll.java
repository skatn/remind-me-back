package skatn.remindmeback.common.scroll;

import java.util.List;

public record Scroll<T> (
        List<T> content,
        Object nextCursor,
        Object nextSubCursor
) {
}
