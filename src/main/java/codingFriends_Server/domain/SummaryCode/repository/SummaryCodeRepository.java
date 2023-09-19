package codingFriends_Server.domain.SummaryCode.repository;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.SummaryCode.entity.ScrapStatus;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SummaryCodeRepository extends JpaRepository<SummaryCode, Long> {
    List<SummaryCode> findSummaryCodesByMemberOrderByCreatedAtDesc(Member member);
    List<SummaryCode> findSummaryCodesByMemberAndScrapStatusOrderByCreatedAtDesc(Member member, ScrapStatus scrapStatus);


}
