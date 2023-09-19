package codingFriends_Server.domain.SummaryCode.service;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.Member.repository.MemberRepository;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeResponseDto;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeTitleContentResponseDto;
import codingFriends_Server.domain.SummaryCode.entity.ScrapStatus;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import codingFriends_Server.domain.SummaryCode.repository.SummaryCodeRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {
    private SummaryCodeRepository summaryCodeRepository;

    @Transactional
    public void saveSummaryCode(SummaryCodeTitleContentResponseDto chat_result, Member member) {
        log.info("save 로직");
        log.info(chat_result.toString());
        log.info("mem");
        log.info(chat_result.getContent());
        log.info("---");
        log.info(chat_result.getTitle());
        log.info("------");
        log.info(member.getSkill_language());

        if (chat_result == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "글이 없습니다.");
        }
        log.info("저장 성공");
        SummaryCode summaryCode = SummaryCode.builder()
                .content(chat_result.getContent())
                .member(member)
                .scrapStatus(ScrapStatus.No)
                .createdAt(LocalDateTime.now())
                .fav_language(member.getSkill_language())
                .title(chat_result.getTitle())
                .build();
        log.info("마지막");
        summaryCodeRepository.save(summaryCode);
    }

    @Transactional
    public void save_likeSummaryCode(SummaryCodeTitleContentResponseDto chat_result, Member member) {
        SummaryCode summaryCode = SummaryCode.builder()
                .content(chat_result.getContent())
                .member(member)
                .fav_language(member.getSkill_language())
                .scrapStatus(ScrapStatus.Yes)
                .createdAt(LocalDateTime.now())
                .title(chat_result.getTitle())
                .build();
        summaryCodeRepository.save(summaryCode);
    }

    public List<SummaryCodeResponseDto> getSummaryCodeByMember(Member member) {
        List<SummaryCode> summaryCodeList = summaryCodeRepository.findSummaryCodesByMemberOrderByCreatedAtDesc(member);
        List<SummaryCodeResponseDto> summaryCodeResponseDtoList = summaryCodeList.stream()
                .map(SummaryCodeResponseDto::new)
                .collect(Collectors.toList());
        return summaryCodeResponseDtoList;
    }

    public List<SummaryCodeResponseDto> getScrapSummaryContents(Member member) {
        List<SummaryCode> summaryCodes = summaryCodeRepository.
                findSummaryCodesByMemberAndScrapStatusOrderByCreatedAtDesc(member, ScrapStatus.Yes);
        List<SummaryCodeResponseDto> summaryCodeResponseDtoList = summaryCodes.stream()
                .map(SummaryCodeResponseDto::new)
                .collect(Collectors.toList());
        return summaryCodeResponseDtoList;
    }
}
