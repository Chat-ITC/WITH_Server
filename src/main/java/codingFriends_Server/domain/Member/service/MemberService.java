package codingFriends_Server.domain.Member.service;

import codingFriends_Server.domain.Member.dto.request.MemberlevelUpdateRequestDto;
import codingFriends_Server.domain.Member.dto.request.MemberFavLanguageRequestDto;
import codingFriends_Server.domain.Member.dto.response.MemberInfoResponseDto;
import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.repository.MemberRepository;
import codingFriends_Server.global.auth.dto.request.SignupRequestDto;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    public Optional<Member> findMemberBySnsId(String snsId) {
        return memberRepository.findBySnsId(snsId);
    }

    @Transactional
    public Member signup(SignupRequestDto signupRequestDto) { // 회원가입
        Member member = signupRequestDto.of();
        memberRepository.save(member);
        return member;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // snsId로 사용자 호출
        return memberRepository.findBySnsId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional // member의 실력을 변경
    public MemberInfoResponseDto updateMemberLevel(String snsId, MemberlevelUpdateRequestDto memberlevelUpdateRequestDto) {
        Member member = memberRepository.findBySnsId(snsId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "member를 찾을 수 없습니다."));

        if (memberlevelUpdateRequestDto.getUser_level() != null) {
            member.setUser_level(memberlevelUpdateRequestDto.getUser_level());
        }
        memberRepository.save(member);
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(member);
        return memberInfoResponseDto;
    }
    @Transactional // member의 선호 언어를 변경
    public MemberInfoResponseDto updateMemberLanguage(String snsId, MemberFavLanguageRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findBySnsId(snsId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "member를 찾을 수 없습니다."));

        if (memberUpdateRequestDto.getSkill_language() != null) {
            member.setSkill_language(memberUpdateRequestDto.getSkill_language());
        }
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(member);
        return memberInfoResponseDto;
    }
}
