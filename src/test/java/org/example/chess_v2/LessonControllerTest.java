package org.example.chess_v2;

import org.example.chess_v2.controller.LessonController;
import org.example.chess_v2.data.LessonData;
import org.example.chess_v2.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)

public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonRepository lessonRepository;

    @Test
    public void testGetAllLessons() throws Exception {
        LessonData lesson = new LessonData();
        lesson.setId(1L);
        lesson.setDate(new Date());
        lesson.setDeadline(new Date());
        lesson.setStudentId(1);
        lesson.setTopic("Test Lesson");

        given(lessonRepository.findAll()).willReturn(Arrays.asList(lesson));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/lesson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].topic").value("Test Lesson"));
    }

    @Test
    public void testCreateLesson() throws Exception {
        String jsonLesson = "{"
                + "\"date\":\"2024-05-02\","
                + "\"deadline\":\"2024-05-10\","
                + "\"studentId\":1111111,"
                + "\"topic\":\"Introduction to Chess Basics\""
                + "}";

        LessonData expectedLesson = new LessonData();
        expectedLesson.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-05-02"));
        expectedLesson.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse("2024-05-10"));
        expectedLesson.setStudentId(1);
        expectedLesson.setTopic("Introduction to Chess Basics");

        given(lessonRepository.existsByTopic("Introduction to Chess Basics")).willReturn(false);
        given(lessonRepository.save(any(LessonData.class))).willReturn(expectedLesson);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLesson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("Introduction to Chess Basics"));
    }

    @Test
    public void testUpdateLessonSuccess() throws Exception {
        Long lessonId = 1L;
        LessonData originalLesson = new LessonData();
        originalLesson.setId(lessonId);
        originalLesson.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"));
        originalLesson.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse("2024-02-01"));
        originalLesson.setStudentId(1);
        originalLesson.setTopic("Initial Topic");

        LessonData updatedLesson = new LessonData();
        updatedLesson.setId(lessonId);
        updatedLesson.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-02"));
        updatedLesson.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse("2024-02-02"));
        updatedLesson.setStudentId(1);
        updatedLesson.setTopic("Updated Topic");

        String jsonLesson = "{"
                + "\"date\":\"2024-01-02\","
                + "\"deadline\":\"2024-02-02\","
                + "\"studentId\":1,"
                + "\"topic\":\"Updated Topic\""
                + "}";

        given(lessonRepository.findById(lessonId)).willReturn(Optional.of(originalLesson));
        given(lessonRepository.existsByTopic("Updated Topic")).willReturn(false);
        given(lessonRepository.save(any(LessonData.class))).willReturn(updatedLesson);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/lesson/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLesson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("Updated Topic"));
    }

    @Test
    public void testDeleteLesson_Success() throws Exception {
        Long lessonId = 1L;
        when(lessonRepository.existsById(lessonId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lesson/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lessonRepository, times(1)).deleteById(lessonId);
    }

    @Test
    public void testDeleteLesson_NotFound() throws Exception {
        Long lessonId = 1L;
        when(lessonRepository.existsById(lessonId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lesson/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

