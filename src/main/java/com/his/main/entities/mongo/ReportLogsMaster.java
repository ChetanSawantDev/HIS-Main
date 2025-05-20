package com.his.main.entities.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "report_logs")
public class ReportLogsMaster {
    @Id
    private String id;
    private String reportName;
    private String jobKey;
    private String requestedBy;
    private String uniqueReportId;
    private LocalDateTime requestedAt;
    private String status; // PENDING / RUNNING / COMPLETED / FAILED
    private String errorMessage;
    private String causedBy;
    private LocalDateTime scheduledFor;
    private LocalDateTime completedAt;
    private String generatedFilePath;
    private Long executionTimeMs;
    private String schedulerStatus;
    private Long memoryConsumedForGeneration;
}
