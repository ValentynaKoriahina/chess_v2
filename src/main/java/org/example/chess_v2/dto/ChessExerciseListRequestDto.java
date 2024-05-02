package org.example.chess_v2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChessExerciseListRequestDto {

    private Long lessonId;
    private Integer difficultyRange;
    private int page;
    private int size;

}