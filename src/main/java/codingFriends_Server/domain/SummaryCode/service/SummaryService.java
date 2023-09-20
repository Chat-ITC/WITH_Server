package codingFriends_Server.domain.SummaryCode.service;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeMainResponseDto;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeResponseDto;
import codingFriends_Server.domain.SummaryCode.Dto.response.SummaryCodeTitleContentResponseDto;
import codingFriends_Server.domain.SummaryCode.entity.ScrapSummaryCode;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import codingFriends_Server.domain.SummaryCode.repository.ScrapSummaryCodeRepository;
import codingFriends_Server.domain.SummaryCode.repository.SummaryCodeRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {
    private final SummaryCodeRepository summaryCodeRepository;
    private final ScrapSummaryCodeRepository scrapSummaryCodeRepository;
    @Transactional
    public SummaryCodeMainResponseDto saveSummaryCode(SummaryCodeTitleContentResponseDto chat_result, String fav_language, Member member) {
        if (chat_result == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "글이 없습니다.");
        }
        SummaryCode summaryCode = SummaryCode.builder()
                .content(chat_result.getContent())
                .member(member)
                .createdAt(LocalDateTime.now())
                .fav_language(fav_language)
                .title(chat_result.getTitle())
                .build();
        summaryCodeRepository.save(summaryCode);

        SummaryCodeMainResponseDto summaryCodeMainResponseDto = new SummaryCodeMainResponseDto(summaryCode);
        return summaryCodeMainResponseDto;
    }

    @Transactional
    public void save_likeSummaryCode(Long id) {
        SummaryCode summaryCode = summaryCodeRepository.findSummaryCodeById(id).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "id값에 맞는 summaryCode가 존재하지 않습니다."));

        if (delete_scrapSummaryCode(summaryCode.getCreatedAt())) {// scrap을 한번 더 누르면 DB에 존재하는지 확인한 뒤, 삭제하고 return false
            //true면 현재 DB에 존재를 안 하는 것.
            ScrapSummaryCode scrapSummaryCode = ScrapSummaryCode.builder()
                    .fav_language(summaryCode.getFav_language())
                    .member(summaryCode.getMember())
                    .title(summaryCode.getTitle())
                    .content(summaryCode.getContent())
                    .createdAt(summaryCode.getCreatedAt())
                    .build();

            scrapSummaryCodeRepository.save(scrapSummaryCode);
        }
    }

    private boolean delete_scrapSummaryCode(LocalDateTime localDateTime) {
        // localDateTime은 밀리초까지 나옴. 그래서 동일한 값이 없다고 판단 하에 검증 필드로 사용
        Optional<ScrapSummaryCode> scrapSummaryCode = scrapSummaryCodeRepository.findScrapSummaryCodeByCreatedAt(localDateTime);
        if (scrapSummaryCode.isPresent()) {
            scrapSummaryCodeRepository.delete(scrapSummaryCode.get());
            return false;
        } else {
            return true;
        }
    }

    public List<SummaryCodeResponseDto> getSummaryCodeByMember(Member member) {
        List<SummaryCode> summaryCodeList = summaryCodeRepository.findSummaryCodesByMemberOrderByCreatedAtDesc(member);
        List<SummaryCodeResponseDto> summaryCodeResponseDtoList = summaryCodeList.stream()
                .map(SummaryCodeResponseDto::new)
                .collect(Collectors.toList());
        return summaryCodeResponseDtoList;
    }

    public List<SummaryCodeResponseDto> getScrapSummaryContents(Member member) {

        List<ScrapSummaryCode> scrapSummaryCodes = scrapSummaryCodeRepository.findScrapSummaryCodesByMemberOrderByCreatedAtDesc(member);
        List<SummaryCodeResponseDto> summaryCodeResponseDtoList = scrapSummaryCodes.stream()
                .map(SummaryCodeResponseDto::new)
                .collect(Collectors.toList());
        return summaryCodeResponseDtoList;
    }
}
