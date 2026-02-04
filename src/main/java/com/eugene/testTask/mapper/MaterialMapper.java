package com.eugene.testTask.mapper;

import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import com.eugene.testTask.model.Material;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MaterialMapper {
    public MaterialResponseDTO toDTO(Material material) {
        if (material == null) {
            return null;
        }
        return new MaterialResponseDTO(
                material.getId(),
                material.getName(),
                material.getDensity(),
                material.getElasticModulus()
        );
    }

    public List<MaterialResponseDTO> toDTO(List<Material> list) {
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
