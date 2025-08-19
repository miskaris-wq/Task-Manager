package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private TaskMapper taskMapper;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Task testTask;

    private Task genTask(String name, String desc,
                         String userEmail, String statusSlug) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow();
        TaskStatus taskStatus = taskStatusRepository.findBySlug(statusSlug)
                .orElseThrow();
        Task task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getCreatedAt))
                .ignore(Select.field(Task::getIndex))
                .ignore(Select.field(Task::getLabels))
                .supply(Select.field(Task::getName), () -> name)
                .supply(Select.field(Task::getDescription), () -> desc)
                .supply(Select.field(Task::getAssignee), () -> user)
                .supply(Select.field(Task::getTaskStatus), () -> taskStatus)
                .create();
        return task;
    }

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        testTask = genTask("Homework", "Do homework",
                "hexlet@example.com", "draft");
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        taskRepository.save(testTask);

    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        List<TaskDTO> list = om.readValue(body, new TypeReference<>() {
        });
        var actual = list.stream()
                .map(p -> taskMapper.map(p))
                .toList();
        var expected = taskRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testFilteredWithNameIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks?name="
                        + testTask.getName()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .and(v -> v.node("[0].title") // почему нужно добавлять [0]?
                        .isEqualTo(testTask.getName()));
    }

    @Test
    public void testFilteredWithAssigneeIdIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks?assigneeId="
                        + testTask.getAssignee().getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .and(v -> v.node("[0].assignee_id")
                        .isEqualTo(testTask.getAssignee().getId()));
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        assertThatJson(body).and(v -> v.node("title").isEqualTo(testTask.getName()));
    }

    @Test
    public void testCreate() throws Exception {
        TaskCreateDTO newTask = new TaskCreateDTO();
        newTask.setTitle("Hello");
        newTask.setStatus("draft");
        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTask));
        var response = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        var task = taskRepository
                .findByName(newTask.getTitle())
                .orElseThrow();
        assertNotNull(task);
        assertThatJson(body)
                .isObject()
                .containsEntry("title", newTask.getTitle())
                .containsEntry("status", "draft");
        assertThat(task.getName()).isEqualTo(newTask.getTitle());
    }

    @Test
    public void testDestroy() throws Exception {
        var name = testTask.getName();
        var request = delete("/api/tasks/" + testTask.getId())
                .with(token);
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(taskRepository.findByName(name).isEmpty());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("title", "Hello");
        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
        var task = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(task.getName()).isEqualTo("Hello");
    }
}
