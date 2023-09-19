package codingFriends_Server.domain.question.service;

import codingFriends_Server.domain.question.entity.Language;
import codingFriends_Server.domain.question.repository.LanguageRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public String save(String type) {
        Language language = Language.builder()
                .type(type)
                .build();
        languageRepository.save(language);
        return type;
    }

    public void updateLanguage(Long id, String type) {
        Language language = languageRepository.findLanguageById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "language를 찾을 수 없습니다."));

        language.setType(type);
        languageRepository.save(language);
    }

    public void deleteLanguage(Long id) {
        Language language = languageRepository.findLanguageById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "language를 찾을 수 없습니다."));
        languageRepository.delete(language);
    }
}
