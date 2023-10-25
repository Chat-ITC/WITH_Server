package codingFriends_Server.domain.summaryCode.repository;

import codingFriends_Server.domain.member.entity.Member;
import codingFriends_Server.domain.summaryCode.entity.SummaryCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryCodeRepository extends JpaRepository<SummaryCode, Long> {
	List<SummaryCode> findSummaryCodesByMemberOrderByCreatedAtDesc(Member member);

	Optional<SummaryCode> findSummaryCodeById(Long id);

}
