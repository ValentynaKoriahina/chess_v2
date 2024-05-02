package org.example.chess_v2.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChessExerciseListResponseDto {
    private List<ChessExerciseListDto> list;
    private int totalPages;
}