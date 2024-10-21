package skatn.remindmeback.subject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import skatn.remindmeback.common.entity.BaseTimeEntity;
import skatn.remindmeback.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update subject set deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Subject extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(length = 6, nullable = false)
    private String color;

    @Builder.Default
    @ColumnDefault("true")
    private boolean isEnableNotification = true;

    @Builder.Default
    @ColumnDefault("'PUBLIC'")
    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<SubjectTag> tags = new ArrayList<>();

    private boolean deleted;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void changeEnableNotification(boolean enable) {
        this.isEnableNotification = enable;
    }

    public void changeVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void changeTags(List<Tag> tags) {
        this.tags.clear();
        for (Tag tag : tags) {
            this.tags.add(
                    SubjectTag.builder()
                            .tag(tag)
                            .subject(this)
                            .build()
            );
        }
    }
}
