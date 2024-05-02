package org.example.chess_v2.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.chess_v2.data.ChessExerciseData;
import org.example.chess_v2.dto.ChessExerciseListDto;
import org.example.chess_v2.dto.ChessExerciseListRequestDto;
import org.example.chess_v2.dto.ChessExerciseListResponseDto;
import org.example.chess_v2.repository.ChessExerciseRepository;
import org.example.chess_v2.repository.specification.ChessExerciseSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

@Service
public class ChessExerciseService {

    private final ChessExerciseRepository repository;

    @Autowired
    public ChessExerciseService(ChessExerciseRepository chessExerciseRepository) {
        this.repository = chessExerciseRepository;
    }

    public ChessExerciseListResponseDto getFilteredExercises(ChessExerciseListRequestDto request) {
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<ChessExerciseData> spec = Specification.where(null);

        if (request.getDifficultyRange() != null) {
            spec = spec.and(ChessExerciseSpecifications.hasDifficultyRange(request.getDifficultyRange()));
        }

        if (request.getLessonId() != null) {
            spec = spec.and(ChessExerciseSpecifications.hasLessonId(request.getLessonId()));
        }

        Page<ChessExerciseData> page = repository.findAll(spec, pageRequest);

        List<ChessExerciseListDto> dtoList = page.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        ChessExerciseListResponseDto response = new ChessExerciseListResponseDto();
        response.setList(dtoList);
        response.setTotalPages(page.getTotalPages());
        return response;
    }

    private ChessExerciseListDto convertToDto(ChessExerciseData exercise) {
        ChessExerciseListDto dto = new ChessExerciseListDto();
        dto.setTopic(exercise.getTopic());
        dto.setDifficultyRange(exercise.getDifficultyRange());
        dto.setStudentLevel(exercise.getStudentLevel());
        return dto;
    }

    public byte[] generateCsvReport(ChessExerciseListRequestDto request) {
        List<ChessExerciseData> exercises = getFilteredExercisesForReport(request);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        try (CSVPrinter csvPrinter = new CSVPrinter(outputStreamWriter, CSVFormat.DEFAULT
                .withHeader("Topic", "Difficulty Range", "Student Level", "Type", "Solution Strategy", "PGN", "Target Skills", "Rate"))) {
            for (ChessExerciseData exercise : exercises) {
                csvPrinter.printRecord(
                        exercise.getTopic(),
                        exercise.getDifficultyRange(),
                        exercise.getStudentLevel(),
                        exercise.getType(),
                        exercise.getSolutionStrategy(),
                        exercise.getPgn(),
                        exercise.getTargetSkills(),
                        exercise.getRate()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to CSV", e);
        }

        return outputStream.toByteArray();
    }

    private List<ChessExerciseData> getFilteredExercisesForReport(ChessExerciseListRequestDto request) {
        Specification<ChessExerciseData> spec = Specification.where(null);
        if (request.getDifficultyRange() != null) {
            spec = spec.and(ChessExerciseSpecifications.hasDifficultyRange(request.getDifficultyRange()));
        }
        if (request.getLessonId() != null) {
            spec = spec.and(ChessExerciseSpecifications.hasLessonId(request.getLessonId()));
        }
        return repository.findAll(spec);
    }

}
