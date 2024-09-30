package skatn.remindmeback.common.repository;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;

public class Functions {

    static public StringTemplate groupConcat(Path<?> path) {
        return Expressions.stringTemplate("group_concat({0})", path);
    }

}
