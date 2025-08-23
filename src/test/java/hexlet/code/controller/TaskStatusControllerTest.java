package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatus.TaskStatusDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    private TaskStatus testTaskStatus;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private User testUser;

    private TaskStatus genStatus(String name, String slug) {
        TaskStatus taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .supply(Select.field(TaskStatus::getName), () -> name)
                .supply(Select.field(TaskStatus::getSlug), () -> slug)
                .create();
        return taskStatus;
    }

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTaskStatus = genStatus("testName", "testSlug");
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        taskStatusRepository.save(testTaskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        List<TaskStatusDTO> statusDTO = om.readValue(body, new TypeReference<>() {
        });
        var actual = statusDTO.stream()
                .map(p -> taskStatusMapper.map(p))
                .toList();
        var expected = taskStatusRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()));
    }

    @Test
    public void testCreate() throws Exception {
        var newTaskStatus = genStatus("Hello", "hello");
        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTaskStatus));
        var response = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        var taskStatus = taskStatusRepository
                .findBySlug(newTaskStatus.getSlug())
                .orElse(null);
        assertNotNull(taskStatus);
        assertThatJson(body)
                .isObject()
                .containsEntry("name", newTaskStatus.getName())
                .containsEntry("slug", newTaskStatus.getSlug());
        assertThat(taskStatus.getName()).isEqualTo(newTaskStatus.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(newTaskStatus.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("name", "newName");
        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request).andExpect(status().isOk());
        var taskStatus = taskStatusRepository.findBySlug("testSlug").orElseThrow();
        assertThat(taskStatus.getName()).isEqualTo("newName");
    }

    @Test
    public void testDestroy() throws Exception {
        var slug = testTaskStatus.getSlug();
        var request = delete("/api/task_statuses/" + testTaskStatus.getId())
                .with(token);
        mockMvc.perform(request).andExpect(status().isNoContent());
        assertThat(taskStatusRepository.findBySlug(slug).isEmpty());
    }
}
