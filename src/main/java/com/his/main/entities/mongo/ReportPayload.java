package com.his.main.entities.mongo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Document(collection = "report_payloads")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportPayload {
    @Id
    private String id;
    private String jobId;
    private Map<String, Object> parameters;

}
