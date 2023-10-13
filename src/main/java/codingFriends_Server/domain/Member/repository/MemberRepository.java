package codingFriends_Server.domain.Member.repository;

import codingFriends_Server.domain.Member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findBySnsId(String snsId); // 유저의 고유한 값 (snsId)로 유저를 찾음

	Optional<Member> findMemberById(Long id);
}
