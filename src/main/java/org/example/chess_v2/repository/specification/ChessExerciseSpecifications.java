package org.example.chess_v2.repository.specification;

import org.example.chess_v2.data.ChessExerciseData;
import org.springframework.data.jpa.domain.Specification;

public class ChessExerciseSpecifications {

    public static Specification<ChessExerciseData> hasDifficultyRange(Integer difficultyRange) {
        return (root, query, criteriaBuilder) -> {
            if (difficultyRange == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("difficultyRange"), difficultyRange);
        };
    }

    public static Specification<ChessExerciseData> hasLessonId(Long lessonId) {
        return (root, query, criteriaBuilder) -> {
            if (lessonId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("lesson").get("id"), lessonId);
        };
    }
}
