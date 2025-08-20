package hexlet.code.dto.TaskStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    private String name;
    private String slug;
}
