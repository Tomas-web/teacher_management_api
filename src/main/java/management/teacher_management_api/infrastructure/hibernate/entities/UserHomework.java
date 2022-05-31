package management.teacher_management_api.infrastructure.hibernate.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users_homeworks")
public class UserHomework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long teacherId;

    @Column
    private long studentId;

    @Column
    private String content;

    @Column
    private LocalDateTime deadline;

    @Column
    private LocalDateTime createdAt;
}
