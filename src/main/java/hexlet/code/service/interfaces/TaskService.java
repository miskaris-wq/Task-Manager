package hexlet.code.service.interfaces;

import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskDTO;
import hexlet.code.dto.Task.TaskParamsDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAll(TaskParamsDTO params);
    TaskDTO getById(Long id);
    TaskDTO create(TaskCreateDTO createDTO);
    TaskDTO update(Long id, TaskUpdateDTO updateDTO);
    void delete(Long id);
}
