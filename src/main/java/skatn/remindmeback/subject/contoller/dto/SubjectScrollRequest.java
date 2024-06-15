package skatn.remindmeback.subject.contoller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import skatn.remindmeback.common.scroll.ScrollRequest;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SubjectScrollRequest extends ScrollRequest<Long, Long> {
}
