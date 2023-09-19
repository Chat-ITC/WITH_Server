package codingFriends_Server.domain.SummeryCode.service;

import codingFriends_Server.domain.Member.entity.Member;
import codingFriends_Server.domain.SummeryCode.entity.ScrapStatus;
import codingFriends_Server.domain.SummeryCode.entity.SummeryCode;
import codingFriends_Server.domain.SummeryCode.repository.SummeryCodeRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SummeryService {
    private SummeryCodeRepository summeryCodeRepository;

    @Transactional
    public void saveSummeryCode(String chat_result, Member member) {
        if (chat_result == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "글이 없습니다.");
        }
        SummeryCode summeryCode = SummeryCode.builder()
                .content(chat_result)
                .member(member)
                .scrapStatus(ScrapStatus.No)
                .createdAt(LocalDateTime.now())
                .build();
        summeryCodeRepository.save(summeryCode);
    }

    @Transactional
    public void save_likeSummeryCode(String chat_result, Member member) {
        SummeryCode summeryCode = SummeryCode.builder()
                .content(chat_result)
                .member(member)
                .scrapStatus(ScrapStatus.Yes)
                .createdAt(LocalDateTime.now())
                .build();
        summeryCodeRepository.save(summeryCode);
    }
}
