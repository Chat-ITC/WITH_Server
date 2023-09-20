package codingFriends_Server.domain.SummaryCode.repository;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryCodeRepository extends JpaRepository<SummaryCode, Long> {
    List<SummaryCode> findSummaryCodesByMemberOrderByCreatedAtDesc(Member member);

    Optional<SummaryCode> findSummaryCodeById(Long id);


}
