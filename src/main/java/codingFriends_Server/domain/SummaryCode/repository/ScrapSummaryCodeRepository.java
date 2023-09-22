package codingFriends_Server.domain.SummaryCode.repository;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.SummaryCode.entity.ScrapSummaryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScrapSummaryCodeRepository extends JpaRepository<ScrapSummaryCode, Long> {
    Optional<ScrapSummaryCode> findScrapSummaryCodeByCreatedAt(LocalDateTime localDateTime);

    List<ScrapSummaryCode> findScrapSummaryCodesByMemberOrderByCreatedAtDesc(Member member);

    Optional<ScrapSummaryCode> findScrapSummaryCodeById(Long id);
}
