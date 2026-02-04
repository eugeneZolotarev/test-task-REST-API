package com.eugene.testTask.service.impl;

import com.eugene.testTask.dto.requestDTO.MaterialRequestDTO;
import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import com.eugene.testTask.exceptions.ResourceAlreadyExistsException;
import com.eugene.testTask.exceptions.ResourceNotFoundException;
import com.eugene.testTask.mapper.MaterialMapper;
import com.eugene.testTask.model.Material;
import com.eugene.testTask.repository.MaterialRepository;
import com.eugene.testTask.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Override
    @Transactional
    public MaterialResponseDTO createMaterial(MaterialRequestDTO materialDTO) {
        if (materialRepository.existsByName(materialDTO.name())) {
            throw new ResourceAlreadyExistsException(
                    "Материал с таким названием уже существует"
            );
        }

        Material material = Material.builder()
                .name(materialDTO.name().trim())
                .density(materialDTO.density())
                .elasticModulus(materialDTO.elasticModulus())
                .build();

        Material savedMaterial = materialRepository.save(material);
        return materialMapper.toDTO(savedMaterial);
    }

    @Override
    public List<MaterialResponseDTO> getAll() {
        List<Material> list = materialRepository.findAll();
        return materialMapper.toDTO(list);
    }

    @Override
    public MaterialResponseDTO getById(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Материала с id=" + id + " не существует")
        );
        return materialMapper.toDTO(material);
    }

    @Override
    @Transactional
    public MaterialResponseDTO updateMaterial(Long id, MaterialRequestDTO materialDTO) {
        Material material = materialRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Материала с id=" + id + " не существует")
        );

        if (!material.getName().equals(materialDTO.name().trim())) {
            if (materialRepository.existsByName(materialDTO.name())) {
                throw new ResourceAlreadyExistsException(
                        "Материал с таким названием уже существует"
                );
            }
        }

        material.setName(materialDTO.name().trim());
        material.setDensity(materialDTO.density());
        material.setElasticModulus(materialDTO.elasticModulus());
        return materialMapper.toDTO(material);
    }

    @Override
    @Transactional
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Материала с id=" + id + " не существует"));
        materialRepository.delete(material);
    }
}
