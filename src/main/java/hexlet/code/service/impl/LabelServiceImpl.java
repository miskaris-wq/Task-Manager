package hexlet.code.service.impl;

import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Label.LabelDTO;
import hexlet.code.dto.Label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.UnprocessableContentException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.interfaces.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;
    private LabelMapper labelMapper;
    private TaskRepository taskRepository;

    @Override
    public List<LabelDTO> getAll() {
        var labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::map)
                .toList();
    }

    @Override
    public LabelDTO getById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    @Override
    public LabelDTO create(LabelCreateDTO createDTO) {
        var label = labelMapper.map(createDTO);
        label = labelRepository.save(label);
        return labelMapper.map(label);
    }

    @Override
    public LabelDTO update(Long id, LabelUpdateDTO updateDTO) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(updateDTO, label);
        label = labelRepository.save(label);
        return labelMapper.map(label);
    }

    @Override
    public void delete(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));

        boolean hasTasks = taskRepository.existsByLabelsId(id);

        if (!hasTasks) {
            labelRepository.deleteById(id);
        } else {
            throw new UnprocessableContentException("Cannot delete label associated with task");
        }
    }
}
