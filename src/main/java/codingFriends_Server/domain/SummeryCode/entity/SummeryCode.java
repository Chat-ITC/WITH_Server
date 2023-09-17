package codingFriends_Server.domain.SummeryCode.entity;

import codingFriends_Server.domain.Member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummeryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    private ScrapStatus scrapStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
