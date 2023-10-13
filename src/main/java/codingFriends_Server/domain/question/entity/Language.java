package codingFriends_Server.domain.question.entity;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String type;

	@OneToMany(mappedBy = "language", cascade = CascadeType.REMOVE)
	private List<Question> questionList = new ArrayList<>();
}
