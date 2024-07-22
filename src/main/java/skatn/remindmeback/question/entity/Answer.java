package skatn.remindmeback.question.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update answer set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String answer;

//    @Column(nullable = false)
    private boolean isAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private boolean deleted;

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isAnswer() {
        return isAnswer;
    }
}
