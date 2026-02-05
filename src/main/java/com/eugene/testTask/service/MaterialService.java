package com.eugene.testTask.service;

import com.eugene.testTask.dto.requestDTO.MaterialRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateMaterialDTO;
import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.print.Pageable;
import java.util.List;

public interface MaterialService {
    MaterialResponseDTO createMaterial(MaterialRequestDTO materialDTO);
    List<MaterialResponseDTO> getAll();
    MaterialResponseDTO getById(Long id);
    MaterialResponseDTO updateMaterial(Long id, UpdateMaterialDTO materialDTO);
    void deleteMaterial(Long id);
}
