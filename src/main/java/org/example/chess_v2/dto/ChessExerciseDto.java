package org.example.chess_v2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChessExerciseDto {
    @NotBlank(message = "Topic is mandatory")
    private String topic;

    @NotNull(message = "Difficulty range is mandatory")
    @Min(value = 1, message = "Difficulty range must be at least 1")
    @Max(value = 5, message = "Difficulty range can be no more than 5")
    private int difficultyRange;

    @NotNull(message = "Student level is mandatory")
    private Integer studentLevel;

    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "^(Standard|Antichess|Atomic|Crazyhouse|Horde|King of the Hill|Racing Kings|Three-check|Tactical|Positional|Endgame)$", message = "Type must be one of the following: Standard, Antichess, Atomic, Crazyhouse, Horde, King of the Hill, Racing Kings, Three-check, Tactical, Positional, Endgame")
    private String mode;

    private String type;

    private String solutionStrategy;

    @NotBlank(message = "PGN is mandatory")
    private String pgn;

    @NotBlank(message = "Target skills are mandatory")
    private String targetSkills;

    private int rate;
}
