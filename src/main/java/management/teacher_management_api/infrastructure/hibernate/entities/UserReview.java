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
@Table(name = "users_reviews")
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long targetUserId;

    @Column
    private long reviewerId;

    @Column
    private int reviewValue;

    @Column
    private String comment;

    @Column
    private LocalDateTime createdAt;
}
