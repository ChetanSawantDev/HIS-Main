package com.his.main.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzJobTriggerDTO {
    private String schedName;
    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String jobClassName;
    private Boolean isDurable;
    private Boolean isNonConcurrent;
    private Boolean isUpdateData;
    private Boolean requestsRecovery;
    private byte[] jobData; // or SerializedObject if applicable

    private String triggerName;
    private String triggerGroup;
    private String triggerState;
    private Integer priority;
    private Long startTime;
    private Long endTime;
    private Long nextFireTime;
    private Long prevFireTime;
}
