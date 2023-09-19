package codingFriends_Server.domain.question.repository;

import codingFriends_Server.domain.question.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findLanguageByType(String type);

    Optional<Language> findLanguageById(Long id);
}
