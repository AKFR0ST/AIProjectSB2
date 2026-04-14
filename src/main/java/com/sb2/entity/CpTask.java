package com.sb2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cp_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(columnDefinition = "jsonb")
    private String extractedData;

    private String error;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
