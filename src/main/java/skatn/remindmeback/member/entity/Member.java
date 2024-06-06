package skatn.remindmeback.member.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import skatn.remindmeback.common.entity.BaseTimeEntity;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(length = 14, nullable = false)
    private String name;

    @Builder.Default
    @ColumnDefault("'ROLE_USER'")
    private String role = "ROLE_USER";

    @Builder.Default
    @ColumnDefault("true")
    private boolean isActive = true;
}
