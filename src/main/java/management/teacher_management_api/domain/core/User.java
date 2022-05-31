package management.teacher_management_api.domain.core;

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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String externalId;

    @Column
    private Long specialityId;

    @Column
    private String fullName;

    @Column
    private String email;

    @Column
    private int roleId;

    @Column
    private String picture;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private String address;

    @Column
    private String contacts;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
