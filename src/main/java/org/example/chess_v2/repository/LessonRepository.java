package org.example.chess_v2.repository;

import org.example.chess_v2.data.LessonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonData, Long> {
    boolean existsByTopic(String topic);

}
