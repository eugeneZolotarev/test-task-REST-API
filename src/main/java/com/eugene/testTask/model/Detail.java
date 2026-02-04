package com.eugene.testTask.model;

import com.eugene.testTask.enums.DetailStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "detail")
@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Detail {
    @Id
    @Column(name = "detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "decimal_number", nullable = false, unique = true)
    private String decimalNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DetailStatus detailStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
