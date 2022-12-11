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
@Table(name = "homeworks_uploads")
public class HomeworkUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long homeworkId;

    @Column
    private long userId;

    @Column
    private String fileName;

    @Column
    private long fileSize;

    @Column
    private String fileType;

    @Column
    private LocalDateTime uploadedAt;
}
