package org.example.chess_v2.controller;

import org.example.chess_v2.repository.LessonRepository;
import org.example.chess_v2.service.JsonParser;
import org.springframework.core.io.Resource;
import org.example.chess_v2.data.ChessExerciseData;
import org.example.chess_v2.dto.ChessExerciseDto;
import org.example.chess_v2.dto.ChessExerciseListRequestDto;
import org.example.chess_v2.dto.ChessExerciseListResponseDto;
import org.example.chess_v2.repository.ChessExerciseRepository;
import org.example.chess_v2.service.ChessExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chess_exercise")
@Validated
public class ChessExerciseController {

    @Autowired
    private ChessExerciseRepository repository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChessExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ChessExerciseData> createChessExercise(@RequestBody @Valid ChessExerciseDto dto) {
        ChessExerciseData newExercise = new ChessExerciseData();
        newExercise.setTopic(dto.getTopic());
        newExercise.setDifficultyRange(dto.getDifficultyRange());
        newExercise.setStudentLevel(dto.getStudentLevel());
        newExercise.setMode(dto.getMode());
        newExercise.setType(dto.getType());
        newExercise.setSolutionStrategy(dto.getSolutionStrategy());
        newExercise.setPgn(dto.getPgn());
        newExercise.setTargetSkills(dto.getTargetSkills());
        newExercise.setRate(dto.getRate());

        repository.save(newExercise);

        return ResponseEntity.ok(newExercise);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChessExerciseData> getChessExercise(@PathVariable Long id) {
        return repository.findById(id)
                .map(exercise -> ResponseEntity.ok().body(exercise))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChessExerciseData> updateChessExercise(@PathVariable Long id, @RequestBody @Validated ChessExerciseDto exerciseDto) {
        return repository.findById(id)
                .map(exercise -> {
                    exercise.setTopic(exerciseDto.getTopic());
                    exercise.setDifficultyRange(exerciseDto.getDifficultyRange());
                    exercise.setStudentLevel(exerciseDto.getStudentLevel());
                    exercise.setMode(exerciseDto.getMode());
                    exercise.setType(exerciseDto.getType());
                    exercise.setSolutionStrategy(exerciseDto.getSolutionStrategy());
                    exercise.setPgn(exerciseDto.getPgn());
                    exercise.setTargetSkills(exerciseDto.getTargetSkills());
                    exercise.setRate(exerciseDto.getRate());

                    ChessExerciseData updatedExercise = repository.save(exercise);
                    return ResponseEntity.ok(updatedExercise);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChessExercise(@PathVariable Long id) {
        return repository.findById(id)
                .map(exercise -> {
                    repository.delete(exercise);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/_list")
    public ChessExerciseListResponseDto getExercises(@RequestBody ChessExerciseListRequestDto request) {
        return exerciseService.getFilteredExercises(request);
    }

    @PostMapping("/_report")
    public ResponseEntity<Resource> generateReport(@RequestBody ChessExerciseListRequestDto request) {
        String filename = "chess_exercises_report.csv";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        byte[] data = exerciseService.generateCsvReport(request);
        return ResponseEntity.ok()
                .headers(headers)
                .body((Resource) new ByteArrayResource(data));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadChessExercises(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
        }

        try {
            JsonParser parser = new JsonParser();
            List<ChessExerciseData> exercises = parser.parse(file.getInputStream());

            int successful = 0;
            int failed = 0;
            for (ChessExerciseData exercise : exercises) {
                if (validateLessonId(exercise.getLessonId())) {
                    repository.save(exercise);
                    successful++;
                } else {
                    failed++;
                }
            }

            return ResponseEntity.ok(Map.of("successful", successful, "failed", failed));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error processing file: " + e.getMessage()));
        }
    }

    private boolean validateLessonId(Long lessonId) {
        return lessonRepository.existsById(lessonId);
    }



}
