package skatn.remindmeback.subject.contoller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import skatn.remindmeback.common.scroll.ScrollRequest;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectScrollRequest extends ScrollRequest<Long, Long> {
    private String title;
    private List<String> tags;
}
