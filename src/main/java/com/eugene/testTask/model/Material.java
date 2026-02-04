package com.eugene.testTask.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "material")
@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Material {
    @Id
    @Column(name = "material_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "density", nullable = false)
    private Double density;

    @Column(name = "elastic_modulus", nullable = false)
    private Double elasticModulus;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    private List<Detail> details;
}
