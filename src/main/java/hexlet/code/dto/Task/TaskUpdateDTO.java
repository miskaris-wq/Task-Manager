package hexlet.code.dto.Task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<Integer> index; //?
    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;
    @Size(min = 1)
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<String> status; // task status id
    private JsonNullable<List<Long>> taskLabelIds;
}
