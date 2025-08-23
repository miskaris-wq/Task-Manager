package hexlet.code.service.interfaces;

import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusDTO;
import hexlet.code.dto.TaskStatus.TaskStatusUpdateDTO;

import java.util.List;

public interface TaskStatusService {
    List<TaskStatusDTO> getAll();
    TaskStatusDTO getById(Long id);
    TaskStatusDTO create(TaskStatusCreateDTO createDTO);
    TaskStatusDTO update(Long id, TaskStatusUpdateDTO updateDTO);
    void delete(Long id);
}