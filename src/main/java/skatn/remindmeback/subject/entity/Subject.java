package skatn.remindmeback.subject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import skatn.remindmeback.common.entity.BaseTimeEntity;
import skatn.remindmeback.member.entity.Member;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(length = 6, nullable = false)
    private String color;

    @ColumnDefault("true")
    private boolean isEnableNotification = true;

    @ManyToOne
    private Member author;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
