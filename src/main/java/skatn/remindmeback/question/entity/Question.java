package skatn.remindmeback.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import skatn.remindmeback.common.entity.BaseTimeEntity;
import skatn.remindmeback.subject.entity.Subject;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    @Column(columnDefinition = "text")
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    private LocalDateTime notificationTime;

    public void changeQuestion(String question) {
        this.question = question;
    }

    public void changeExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void changeQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public void changeAnswers(Set<Answer> answers) {
        this.answers.clear();
        answers.forEach(answer -> {
            this.answers.add(answer);
            answer.setQuestion(this);
        });
    }
}
