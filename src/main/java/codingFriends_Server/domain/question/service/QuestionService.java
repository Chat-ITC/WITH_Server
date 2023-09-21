package codingFriends_Server.domain.question.service;

import codingFriends_Server.domain.question.dto.request.QuestionRequestDto;
import codingFriends_Server.domain.question.dto.request.QuestionUpdateRequestDto;
import codingFriends_Server.domain.question.dto.response.QuestionResponseDto;
import codingFriends_Server.domain.question.dto.response.QuestionTitleResponseDto;
import codingFriends_Server.domain.question.entity.Language;
import codingFriends_Server.domain.question.entity.Question;
import codingFriends_Server.domain.question.repository.LanguageRepository;
import codingFriends_Server.domain.question.repository.QuestionRepository;
import codingFriends_Server.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final LanguageRepository languageRepository;

    @Transactional
    public void saveQuestion(QuestionRequestDto questionRequestDto, String type) {
        Language language = languageRepository.findLanguageByType(type).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "language가 존재하지 않습니다."));

        Question question = Question.builder()
                .title(questionRequestDto.getTitle())
                .content(questionRequestDto.getContent())
                .answer(questionRequestDto.getAnswer())
                .language(language)
                .build();
        questionRepository.save(question);
    }

    public List<QuestionTitleResponseDto> findRandomQuestionsByLanguage(String type, String level) {
        Language language = languageRepository.findLanguageByType(type).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "language가 존재하지 않습니다."));

        List<Question> userLevelQuestions = language.getQuestionList().stream()
                .filter(question -> question.getLevel().equals(level))
                .collect(Collectors.toList());

        Collections.shuffle(userLevelQuestions);

        // 무작위로 4개의 질문을 선택합니다.
        return userLevelQuestions.stream()
                .limit(4)
                .map(QuestionTitleResponseDto::new)
                .collect(Collectors.toList());
    }

    public QuestionResponseDto getQuestionFromTitle(Long id) {
        Question question = questionRepository.findQuestionById(id).orElseThrow(
                () -> new CustomException(HttpStatus.BAD_REQUEST, "question이 존재하지 않습니다."));
        QuestionResponseDto questionResponseDto = new QuestionResponseDto(question);
        return questionResponseDto;
    }

    public void updateQuestion(Long id, QuestionUpdateRequestDto questionUpdateRequestDto) {
        Question question = questionRepository.findQuestionById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "question이 존재하지 않습니다."));

        if (questionUpdateRequestDto.getTitle() != null) {
            question.setTitle(questionUpdateRequestDto.getTitle());
        }
        if (questionUpdateRequestDto.getContent() != null) {
            question.setContent(questionUpdateRequestDto.getContent());
        }
        if (questionUpdateRequestDto.getAnswer() != null) {
            question.setAnswer(questionUpdateRequestDto.getAnswer());
        }
        questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        Question question = questionRepository.findQuestionById(id).orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "question이 존재하지 않습니다."));
        questionRepository.delete(question);
    }
}
