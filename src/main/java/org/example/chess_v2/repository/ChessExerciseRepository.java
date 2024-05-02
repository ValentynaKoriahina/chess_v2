package org.example.chess_v2.repository;

import org.example.chess_v2.data.ChessExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessExerciseRepository extends JpaRepository<ChessExerciseData, Long>, JpaSpecificationExecutor<ChessExerciseData> {
}
