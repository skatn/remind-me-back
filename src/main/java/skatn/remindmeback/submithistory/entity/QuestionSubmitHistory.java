package skatn.remindmeback.submithistory.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import skatn.remindmeback.common.entity.BaseEntity;
import skatn.remindmeback.question.entity.Question;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSubmitHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryStatus status;

    @Column(nullable = false)
    private String submittedAnswer;
}
