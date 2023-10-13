package codingFriends_Server.domain.question.service;

import codingFriends_Server.domain.question.entity.Language;
import codingFriends_Server.domain.question.repository.LanguageRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {

	private final LanguageRepository languageRepository;

	public String save(String type) { // language 저장
		checkLanguageNameDuplication(type);
		Language language = Language.builder()
			.type(type)
			.build();
		languageRepository.save(language);
		return type;
	}

	public void updateLanguage(Long id, String type) { // language 수정
		checkLanguageNameDuplication(type);
		Language language = languageRepository.findLanguageById(id)
			.orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "language를 찾을 수 없습니다."));

		language.setType(type);
		languageRepository.save(language);
	}

	private void checkLanguageNameDuplication(String type) { // language 중복 방지
		Optional<Language> language_op = languageRepository.findLanguageByType(type);
		if (language_op.isPresent()) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 type명입니다.");
		}
	}

	public void deleteLanguage(Long id) {
		Language language = languageRepository.findLanguageById(id)
			.orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "language를 찾을 수 없습니다."));
		languageRepository.delete(language);
	}
}
