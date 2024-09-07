package skatn.remindmeback.common.fcm.entity;

import jakarta.persistence.*;
import lombok.*;
import skatn.remindmeback.member.entity.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(unique = true)
    private String token;

    private String refreshTokenGroup;
}
