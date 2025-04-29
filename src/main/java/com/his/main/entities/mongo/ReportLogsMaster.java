package com.his.main.entities.mongo;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "report_logs")
public class ReportLogsMaster {
    @Id
    private String id;
    private String reportName;
    private String requestedBy;
    private LocalDateTime requestedAt;
    private String status; // PENDING / RUNNING / COMPLETED / FAILED
    private String errorMessage;
    private String causedBy;
    private LocalDateTime completedAt;
    private String generatedFilePath;
    private Long executionTimeMs;

}
