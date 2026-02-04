package com.eugene.testTask.mapper;

import com.eugene.testTask.dto.responseDTO.DetailResponseDTO;
import com.eugene.testTask.model.Detail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetailMapper {

    private final MaterialMapper materialMapper;

    public DetailResponseDTO toDTO(Detail detail) {
        return new DetailResponseDTO(
                detail.getId(),
                detail.getName(),
                detail.getDecimalNumber(),
                materialMapper.toDTO(detail.getMaterial()),
                detail.getWeight(),
                detail.getDetailStatus(),
                detail.getCreatedAt(),
                detail.getUpdatedAt()
        );
    }
}
