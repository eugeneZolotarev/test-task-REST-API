package com.eugene.testTask.controller;

import com.eugene.testTask.dto.requestDTO.DetailRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateDetailDTO;
import com.eugene.testTask.dto.responseDTO.DetailResponseDTO;
import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import com.eugene.testTask.enums.DetailStatus;
import com.eugene.testTask.exceptions.ActionNotAllowedException;
import com.eugene.testTask.exceptions.ResourceAlreadyExistsException;
import com.eugene.testTask.exceptions.ResourceNotFoundException;
import com.eugene.testTask.service.DetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DetailController.class)
public class TestDetailController {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DetailService detailService;

    @Test
    void createDetail_ShouldReturnCreated_WhenValidRequest() throws Exception {
        DetailRequestDTO request = new DetailRequestDTO(
                "Подшипник",
                "АБВГ.123456.789",
                1L,
                100.,
                DetailStatus.IN_DESIGN
        );
        MaterialResponseDTO material = new MaterialResponseDTO(
                1L,
                "steel",
                1400.,
                1.4e9
        );
        DetailResponseDTO response = new DetailResponseDTO(
                1L,
                "Подшипник",
                "АБВГ.123456.789",
                material,
                100.,
                DetailStatus.IN_DESIGN,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(detailService.createDetail(any(DetailRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Подшипник"))
                .andExpect(jsonPath("$.material.name").value("steel"))
                .andExpect(jsonPath("$.detailStatus").value("IN_DESIGN"));
    }

    @Test
    void createDetail_WhenIncorrectDecimalNumberFormat_ThenBadRequest() throws Exception {
        DetailRequestDTO request = new DetailRequestDTO(
                "Подшипник",
                "АБВГ.ABCD...231231.2",
                1L,
                100.,
                DetailStatus.IN_DESIGN
        );
        mockMvc.perform(post("/api/v1/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Error: decimalNumber: Децимальный номер должен быть в формате АБВГ.123456.789"));
    }

    @Test
    void createDetail_ShouldReturnConflict_WhenDetailAlreadyExists() throws Exception{
        DetailRequestDTO request = new DetailRequestDTO(
                "Подшипник",
                "АБВГ.123456.789",
                1L,
                100.,
                DetailStatus.IN_DESIGN
        );
        when(detailService.createDetail(request))
                .thenThrow(new ResourceAlreadyExistsException("Деталь с таким названием уже существует"));

        mockMvc.perform(post("/api/v1/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Деталь с таким названием уже существует"));
    }

    @Test
    void getById_ShouldReturnOk_WhenDetailExists() throws Exception{
        MaterialResponseDTO material = new MaterialResponseDTO(
                1L,
                "steel",
                1400.,
                1.4e9
        );
        DetailResponseDTO response = new DetailResponseDTO(
                1L,
                "Подшипник",
                "АБВГ.123456.789",
                material,
                100.,
                DetailStatus.IN_DESIGN,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(detailService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/details/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Подшипник"))
                .andExpect(jsonPath("$.material.name").value("steel"))
                .andExpect(jsonPath("$.detailStatus").value("IN_DESIGN"));
    }

    @Test
    void getById_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        when(detailService.getById(1L)).thenThrow(new ResourceNotFoundException("Детали с id=1 не существует"));

        mockMvc.perform(get("/api/v1/details/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Детали с id=1 не существует"));
    }

    @Test
    void updateDetail_ShouldReturnOk_WhenValidRequest() throws Exception {
        UpdateDetailDTO request = new UpdateDetailDTO(
                "Новый подшипник",
                null,
                null,
                null,
                null
        );
        MaterialResponseDTO material = new MaterialResponseDTO(
                1L,
                "steel",
                1400.,
                1.4e9
        );
        DetailResponseDTO response = new DetailResponseDTO(
                1L,
                "Новый подшипник",
                "АБВГ.123456.789",
                material,
                100.,
                DetailStatus.IN_DESIGN,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(detailService.updateDetail(eq(1L), any(UpdateDetailDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/details/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новый подшипник"));
    }

    @Test
    void updateDetail_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        UpdateDetailDTO request = new UpdateDetailDTO("Новый подшипник",
                null,
                null,
                null,
                null);

        when(detailService.updateDetail(eq(1L), any(UpdateDetailDTO.class)))
                .thenThrow(new ResourceNotFoundException("Детали с id=1 не существует"));

        mockMvc.perform(patch("/api/v1/details/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Детали с id=1 не существует"));
    }

    @Test
    void updateStatus_ShouldReturnNoContent_WhenValidRequest() throws Exception {
        mockMvc.perform(patch("/api/v1/details/1/status")
                        .param("detailStatus", "READY"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateStatus_ShouldReturnForbidden_WhenActionNotAllowed() throws Exception {
        doThrow(new ActionNotAllowedException("Нельзя разархивировать деталь"))
                .when(detailService).updateStatus(1L, DetailStatus.IN_DESIGN);

        mockMvc.perform(patch("/api/v1/details/1/status")
                        .param("detailStatus", "IN_DESIGN"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value("Нельзя разархивировать деталь"));
    }
}
