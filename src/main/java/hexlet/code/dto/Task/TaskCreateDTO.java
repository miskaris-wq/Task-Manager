package hexlet.code.dto.Task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    private JsonNullable<Integer> index; //?
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @NotBlank
    @Size(min = 1)
    private String title;
    private JsonNullable<String> content;
    @NotNull
    private String status;
    private List<Long> taskLabelIds = new ArrayList<>();
}
