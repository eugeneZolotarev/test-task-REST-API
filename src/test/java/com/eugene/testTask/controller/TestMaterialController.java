package com.eugene.testTask.controller;

import com.eugene.testTask.dto.requestDTO.MaterialRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateMaterialDTO;
import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import com.eugene.testTask.exceptions.ResourceAlreadyExistsException;
import com.eugene.testTask.exceptions.ResourceNotFoundException;
import com.eugene.testTask.service.MaterialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MaterialController.class)
public class TestMaterialController {
    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MaterialService materialService;

    @Test
    void createMaterial_ShouldReturnCreated_WhenValidRequest() throws Exception{
        MaterialRequestDTO request = new MaterialRequestDTO(
                "steel",
                1400.,
                1.4e9
        );
        MaterialResponseDTO response = new MaterialResponseDTO(
                1L,
                "steel",
                1400.,
                1.4e9
        );
        when(materialService.createMaterial(any(MaterialRequestDTO.class))).thenReturn(response);

        mockMVC.perform(post("/api/v1/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createMaterial_ShouldReturnBadRequest_WhenEmptyName() throws Exception{
        MaterialRequestDTO request = new MaterialRequestDTO(
                "",
                1400.,
                1.4e9
        );
        mockMVC.perform(post("/api/v1/materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void createMaterial_ShouldReturnConflict_WhenMaterialAlreadyExists() throws Exception{
        MaterialRequestDTO request = new MaterialRequestDTO(
                "steel",
                1400.,
                1.4e9
        );
        when(materialService.createMaterial(request))
                .thenThrow(new ResourceAlreadyExistsException("Материал с таким названием уже существует"));

        mockMVC.perform(post("/api/v1/materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Материал с таким названием уже существует"));
    }

    @Test
    void getById_ShouldReturnOk_WhenMaterialExists() throws Exception{
        when(materialService.getById(1L)).thenReturn(new MaterialResponseDTO(
                1L,
                "steel",
                1400.,
                1.4e9
        ));
        mockMVC.perform(get("/api/v1/materials/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("steel"));
    }

    @Test
    void getById_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        when(materialService.getById(1L)).thenThrow(new ResourceNotFoundException("Материала с id=1 не существует"));

        mockMVC.perform(get("/api/v1/materials/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Материала с id=1 не существует"));
    }

    @Test
    void updateMaterial_ShouldReturnOk_WhenValidRequest() throws Exception {
        UpdateMaterialDTO request = new UpdateMaterialDTO(
                "new steel",
                1500.,
                null
        );
        MaterialResponseDTO response = new MaterialResponseDTO(
                1L,
                "new steel",
                1500.,
                1.4e9
        );
        when(materialService.updateMaterial(eq(1L), any(UpdateMaterialDTO.class))).thenReturn(response);

        mockMVC.perform(patch("/api/v1/materials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("new steel"))
                .andExpect(jsonPath("$.density").value(1500.));
    }

    @Test
    void updateMaterial_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        UpdateMaterialDTO request = new UpdateMaterialDTO("steel", null, null);
        when(materialService.updateMaterial(eq(1L), any(UpdateMaterialDTO.class)))
                .thenThrow(new ResourceNotFoundException("Материала с id=1 не существует"));

        mockMVC.perform(patch("/api/v1/materials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Материала с id=1 не существует"));
    }

    @Test
    void updateMaterial_ShouldReturnConflict_WhenNameAlreadyExists() throws Exception {
        UpdateMaterialDTO request = new UpdateMaterialDTO("existingName", null, null);
        when(materialService.updateMaterial(eq(1L), any(UpdateMaterialDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("Материал с таким названием уже существует"));

        mockMVC.perform(patch("/api/v1/materials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Материал с таким названием уже существует"));
    }

    @Test
    void deleteMaterial_ShouldReturnNoContent_WhenMaterialExists() throws Exception {
        mockMVC.perform(delete("/api/v1/materials/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMaterial_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        doThrow(new ResourceNotFoundException("Материала с id=1 не существует"))
                .when(materialService).deleteMaterial(1L);

        mockMVC.perform(delete("/api/v1/materials/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Материала с id=1 не существует"));
    }
}
