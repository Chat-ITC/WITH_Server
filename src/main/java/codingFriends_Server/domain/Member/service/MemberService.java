package codingFriends_Server.domain.Member.service;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.repository.MemberRepository;
import codingFriends_Server.global.auth.dto.request.SignupRequestDto;
import codingFriends_Server.global.auth.dto.response.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Optional<Member> findMemberBySnsId(String snsId) {
        return memberRepository.findBySnsId(snsId);
    }

    @Transactional
    public Member signup(SignupRequestDto signupRequestDto) {
        Member member = signupRequestDto.of();
        memberRepository.save(member);
        return member;
    }
}
