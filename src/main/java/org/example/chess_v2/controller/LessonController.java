package org.example.chess_v2.controller;

import org.example.chess_v2.data.LessonData;
import org.example.chess_v2.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping
    public List<LessonData> getAllLessons() {
        return lessonRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@RequestBody @Valid LessonData lesson) {
        if (lessonRepository.existsByTopic(lesson.getTopic())) {
            return ResponseEntity.badRequest().body("LessonData with topic '" + lesson.getTopic() + "' already exists");
        }
        LessonData newLesson = lessonRepository.save(lesson);
        return ResponseEntity.ok(newLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Long id, @RequestBody @Valid LessonData lesson) {
        Optional<LessonData> existingLessonOptional = lessonRepository.findById(id);
        if (existingLessonOptional.isPresent()) {
            LessonData existingLesson = existingLessonOptional.get();

            if (!existingLesson.getTopic().equals(lesson.getTopic()) &&
                    lessonRepository.existsByTopic(lesson.getTopic())) {
                return ResponseEntity.badRequest().body("Lesson with topic '" + lesson.getTopic() + "' already exists");
            }
            existingLesson.setDate(lesson.getDate());
            existingLesson.setDeadline(lesson.getDeadline());
            existingLesson.setStudentId(lesson.getStudentId());
            existingLesson.setTopic(lesson.getTopic());
            LessonData updatedLesson = lessonRepository.save(existingLesson);
            return ResponseEntity.ok(updatedLesson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}