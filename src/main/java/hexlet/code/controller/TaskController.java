package hexlet.code.controller;

import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskDTO;
import hexlet.code.dto.Task.TaskParamsDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.service.interfaces.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params) {
        var tasks = taskService.getAll(params);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @GetMapping(path = "/{id}")
    public TaskDTO show(@PathVariable("id") long id) {
        return taskService.getById(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody @Valid TaskCreateDTO data) {
        return taskService.create(data);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable("id") long id) {
        taskService.delete(id);
    }

    @PutMapping(path = "/{id}")
    public TaskDTO update(@PathVariable("id") long id, @RequestBody @Valid TaskUpdateDTO data) {
        return taskService.update(id, data);
    }
}
