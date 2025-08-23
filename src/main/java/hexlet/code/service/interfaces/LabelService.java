package hexlet.code.service.interfaces;

import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Label.LabelDTO;
import hexlet.code.dto.Label.LabelUpdateDTO;

import java.util.List;

public interface LabelService {
    List<LabelDTO> getAll();
    LabelDTO getById(Long id);
    LabelDTO create(LabelCreateDTO createDTO);
    LabelDTO update(Long id, LabelUpdateDTO updateDTO);
    void delete(Long id);
}