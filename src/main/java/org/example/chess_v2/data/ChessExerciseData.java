package org.example.chess_v2.data;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "chess_exercise", indexes = {
        @Index(name = "idx_difficulty_range", columnList = "difficultyRange"),
        @Index(name = "idx_lesson_id", columnList = "lesson_id")
})
@Getter
@Setter
public class ChessExerciseData {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String topic;
        private int difficultyRange;
        private int studentLevel;
        private String mode;
        private String type;
        private String solutionStrategy;
        private String pgn;
        private String targetSkills;
        private int rate;

        @Column(name = "lesson_id", nullable = true)
        private Long lessonId;
}