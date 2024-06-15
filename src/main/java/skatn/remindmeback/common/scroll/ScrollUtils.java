package skatn.remindmeback.common.scroll;

import java.util.List;
import java.util.function.Function;

public class ScrollUtils {
    public static <T, R> R getNextCursor(List<T> contents, int size, Function<T, R> getCursor) {
        R nextCursor = null;
        if(contents.size() > size) {
            nextCursor = getCursor.apply(contents.get(contents.size() - 1));
            contents.remove(contents.size() - 1);
        }

        return nextCursor;
    }
}
