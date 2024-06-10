package skatn.remindmeback.question.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import skatn.remindmeback.common.scroll.ScrollRequest;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionScrollRequest extends ScrollRequest<Long, Long> {
    @NotNull private Long subjectId;
}
