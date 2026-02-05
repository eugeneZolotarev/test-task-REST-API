package com.eugene.testTask.service.impl;

import com.eugene.testTask.dto.requestDTO.DetailRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateDetailDTO;
import com.eugene.testTask.dto.responseDTO.DetailResponseDTO;
import com.eugene.testTask.enums.DetailStatus;
import com.eugene.testTask.exceptions.ActionNotAllowedException;
import com.eugene.testTask.exceptions.ResourceAlreadyExistsException;
import com.eugene.testTask.exceptions.ResourceNotFoundException;
import com.eugene.testTask.mapper.DetailMapper;
import com.eugene.testTask.model.Detail;
import com.eugene.testTask.model.Material;
import com.eugene.testTask.repository.DetailRepository;
import com.eugene.testTask.repository.MaterialRepository;
import com.eugene.testTask.service.DetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetailServiceImpl implements DetailService {

    private final DetailRepository detailRepository;
    private final DetailMapper detailMapper;
    private final MaterialRepository materialRepository;

    @Override
    @Transactional
    public DetailResponseDTO createDetail(DetailRequestDTO detailDTO) {
        String normalizedDecimal = detailDTO.decimalNumber().trim().toUpperCase();

        if (detailRepository.existsByDecimalNumber(normalizedDecimal)) {
            throw new ResourceAlreadyExistsException("Деталь с децимальным номером: " + normalizedDecimal + " уже существует");
        }

        Material material = materialRepository.findById(detailDTO.materialId()).orElseThrow(
                () -> new ResourceAlreadyExistsException("Материал с id=" + detailDTO.materialId() + " не существует")
        );

        Detail detail = Detail.builder()
                .name(detailDTO.name())
                .decimalNumber(normalizedDecimal)
                .material(material)
                .weight(detailDTO.weight())
                .detailStatus(detailDTO.detailStatus() != null ? detailDTO.detailStatus() : DetailStatus.IN_DESIGN)
                .build();

        Detail savedDetail = detailRepository.save(detail);
        return detailMapper.toDTO(savedDetail);
    }

    @Override
    public Page<DetailResponseDTO> getAll(Pageable pageable) {
        return detailRepository.findAll(pageable)
                .map(detailMapper::toDTO);
    }

    @Override
    public DetailResponseDTO getById(Long id) {
        Detail detail = detailRepository.getDetailById(id).orElseThrow(
                () -> new ResourceAlreadyExistsException("Деталь с id=" + id + " не существует")
        );
        return detailMapper.toDTO(detail);
    }

    @Override
    @Transactional
    public DetailResponseDTO updateDetail(Long id, UpdateDetailDTO updateDetailDTO) {
        Detail detail = detailRepository.getDetailById(id).orElseThrow(
                () -> new ResourceAlreadyExistsException("Деталь с id=" + id + " не существует")
        );

        if (updateDetailDTO == null) return detailMapper.toDTO(detail);

        if (updateDetailDTO.decimalNumber() != null) {
            String newDecimal = updateDetailDTO.decimalNumber().trim().toUpperCase();
            if (!newDecimal.equals(detail.getDecimalNumber()) &&
                    detailRepository.existsByDecimalNumberAndIdNot(newDecimal, id)) {
                throw new ResourceAlreadyExistsException("Такой номер " + newDecimal + " уже занят");
            }
            detail.setDecimalNumber(newDecimal);
        }

        if (updateDetailDTO.materialId() != null) {
            if (!updateDetailDTO.materialId().equals(detail.getMaterial().getId())) {
                Material newMaterial = materialRepository.findById(updateDetailDTO.materialId())
                        .orElseThrow(() -> new ResourceNotFoundException("Материал с id " + updateDetailDTO.materialId() + " не найден"));
                detail.setMaterial(newMaterial);
            }
        }

        if (updateDetailDTO.name() != null) detail.setName(updateDetailDTO.name());
        if (updateDetailDTO.weight() != null) detail.setWeight(updateDetailDTO.weight());
        if (updateDetailDTO.detailStatus() != null) detail.setDetailStatus(updateDetailDTO.detailStatus());

        return detailMapper.toDTO(detail);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, DetailStatus status) {
        Detail detail = detailRepository.getDetailById(id).orElseThrow(
                () -> new ResourceAlreadyExistsException("Деталь с id=" + id + " не существует")
        );
        if (detail.getDetailStatus() == DetailStatus.ARCHIVED) {
            throw new ActionNotAllowedException("Нельзя разархивировать деталь");
        }
        detail.setDetailStatus(status);
    }
}
