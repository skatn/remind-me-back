package skatn.remindmeback.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    private String questionImage;

    @Column(columnDefinition = "text")
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    private LocalDateTime notificationTime;

    @Builder.Default
    @ColumnDefault("30")
    @Column(name = "intervals")
    private int interval = 30; // 분 단위

    public void changeQuestion(String question) {
        this.question = question;
    }

    public void changeQuestionImage(String questionImage) {
        this.questionImage = questionImage;
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

    public void updateNotificationTime() {
        this.notificationTime = LocalDateTime.now().plusMinutes(this.interval);
    }

    public void increaseInterval() {
        this.interval = Math.min((int) (this.interval * 1.5), 525600); // 최대 1년
    }

    public void decreaseInterval() {
        this.interval = Math.max((int) (this.interval * 1.5), 30);  // 최소 30분
    }
}
