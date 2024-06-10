package skatn.remindmeback.question.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String answer;

//    @Column(nullable = false)
    private boolean isAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isAnswer() {
        return isAnswer;
    }
}
