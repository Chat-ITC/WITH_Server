package codingFriends_Server.domain.question.repository;

import codingFriends_Server.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findQuestionByTitle(String title);

    Optional<Question> findQuestionById(Long id);
}
