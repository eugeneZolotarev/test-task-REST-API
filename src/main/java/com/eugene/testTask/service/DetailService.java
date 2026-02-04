package com.eugene.testTask.service;

import com.eugene.testTask.dto.requestDTO.DetailRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateDetailDTO;
import com.eugene.testTask.dto.responseDTO.DetailResponseDTO;
import com.eugene.testTask.enums.DetailStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DetailService {
    DetailResponseDTO createDetail(DetailRequestDTO detailDTO);
    Page<DetailResponseDTO> getAll(Pageable pageable);
    DetailResponseDTO getById(Long id);
    DetailResponseDTO updateDetail(Long id, UpdateDetailDTO detailDTO);
    void updateStatus(Long id, DetailStatus status);
}
