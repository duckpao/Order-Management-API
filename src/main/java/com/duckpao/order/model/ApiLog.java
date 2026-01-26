package com.duckpao.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private String path;

    @Column(columnDefinition = "LONGTEXT")
    private String requestBody;

    @Column(columnDefinition = "LONGTEXT")
    private String responseBody;

    private Integer status;
    private Long executionTimeMs;

    private LocalDateTime createdAt;
}
