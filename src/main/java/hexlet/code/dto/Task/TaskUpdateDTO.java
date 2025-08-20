package hexlet.code.dto.Task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private Integer index;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @Size(min = 1)
    private String title;
    private String content;
    private String status;
    private List<Long> taskLabelIds;
}
