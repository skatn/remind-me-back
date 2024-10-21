package skatn.remindmeback.common.fixture;

import skatn.remindmeback.subject.entity.Tag;

import java.util.Arrays;
import java.util.List;

public class TagFixture {

    public static Tag java() {
        return Tag.builder().id(1L).name("java").build();
    }

    public static Tag programming() {
        return Tag.builder().id(2L).name("programming").build();
    }

    public static List<Tag> tagsWithoutId(String... tags) {
        return Arrays.stream(tags).map(tag -> Tag.builder().name(tag).build()).toList();
    }
}
