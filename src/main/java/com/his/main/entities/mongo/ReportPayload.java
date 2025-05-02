package com.his.main.entities.mongo;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Map;

@Document(collection = "report_payloads")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportPayload {
    @Id
    private String id;
    @Field(name="jobId")
    private String jobKey;
    private String uniqueJobName;
    private String jobName;
    private String cronExpression;
    private Map<String, Object> parameters;
    private String webPageUrl;
}
