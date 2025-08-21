package hexlet.code.mapper;

import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "slugToStatus")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "forLabels")
    public abstract Task map(TaskCreateDTO data);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus", target = "status", qualifiedByName = ("statusToSlug"))
    @Mapping(source = "labels", target = "taskLabelIds", qualifiedByName = ("reverseLabels"))
    public abstract TaskDTO map(Task data);

    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = ("slugToStatus"))
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "forLabels")
    public abstract Task map(TaskDTO data);

    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "mapAssignee")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "slugToStatus")
    @Mapping(target = "name", source = "title")
    public abstract void update(TaskUpdateDTO data, @MappingTarget Task task);

    @Named("reverseLabels")
    public List<Long> reverseLabels(Set<Label> labels) {
        if (labels == null || labels.isEmpty()) {
            return Collections.emptyList();
        }
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toList());
    }

    @Named("forLabels")
    public Set<Label> forLabels(List<Long> labelIds) {
        if (labelIds == null || labelIds.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(labelRepository.findAllById(labelIds));
    }

    @Named("slugToStatus")
    public TaskStatus slugToStatus(String data) {
        var status = taskStatusRepository.findBySlug(data).orElseThrow();
        return status;
    }

    @Named("statusToSlug")
    public String statusToSlug(TaskStatus data) {
        var slug = data.getSlug();
        return slug;
    }

    @Named("mapAssignee")
    public User mapAssignee(JsonNullable<Long> assigneeId) {
        if (!assigneeId.isPresent() || assigneeId == null) {
            return null;
        }
        if (assigneeId.get() == null) {
            return null;
        }
        return userRepository.findById(assigneeId.get())
                .orElseThrow();
    }

}
