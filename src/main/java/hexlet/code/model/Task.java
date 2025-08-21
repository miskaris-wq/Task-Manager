package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Task implements BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ToString.Include
    private int index;

    @NotBlank
    @ToString.Include
    @Size(min = 1)
    private String name;

    @ToString.Include
    private String description;

    @NotNull
    @ToString.Include
    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus taskStatus;

    @ToString.Include
    @ManyToOne
    private User assignee;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Label> labels = new HashSet<>();

}
