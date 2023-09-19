package codingFriends_Server.domain.SummaryCode.service;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.SummaryCode.entity.ScrapStatus;
import codingFriends_Server.domain.SummaryCode.entity.SummaryCode;
import codingFriends_Server.domain.SummaryCode.repository.SummaryCodeRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private SummaryCodeRepository summaryCodeRepository;

    @Transactional
    public void saveSummaryCode(String chat_result, Member member) {
        if (chat_result == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "글이 없습니다.");
        }
        SummaryCode summaryCode = SummaryCode.builder()
                .content(chat_result)
                .member(member)
                .scrapStatus(ScrapStatus.No)
                .createdAt(LocalDateTime.now())
                .build();
        summaryCodeRepository.save(summaryCode);
    }

    @Transactional
    public void save_likeSummaryCode(String chat_result, Member member) {
        SummaryCode summaryCode = SummaryCode.builder()
                .content(chat_result)
                .member(member)
                .scrapStatus(ScrapStatus.Yes)
                .createdAt(LocalDateTime.now())
                .build();
        summaryCodeRepository.save(summaryCode);
    }
}
