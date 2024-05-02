package org.example.chess_v2.data;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
public class LessonData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.DATE)
    private Date deadline;

    private int studentId;
    private String topic;
}
