package skatn.remindmeback.common.scroll;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class ScrollRequest<C, SC> {
    private C cursor;
    private SC subCursor;

    @Builder.Default
    @Min(1) @Max(100)
    private int size = 10;
}
