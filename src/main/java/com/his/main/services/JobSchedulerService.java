package com.his.main.services;

import com.his.main.authEntities.UserMaster;
import com.his.main.configuration.AuthenticationUtility;
import com.his.main.dto.QuartzJobTriggerDTO;
import com.his.main.dto.UserAuthDetails;
import com.his.main.quartzJob.ReportJob;
import com.his.main.entities.mongo.ReportLogsMaster;
import com.his.main.entities.mongo.ReportPayload;
import com.his.main.repositories.UserAuthenticationMasterRepo;
import com.his.main.repositories.mongo.ReportLogRepository;
import com.his.main.repositories.mongo.ReportPayloadRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.his.core.utiltiy.CommonUtility;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class JobSchedulerService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ReportPayloadRepo reportPayloadRepo;
    @Autowired
    private ReportLogRepository reportLogRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuthenticationUtility authenticationUtility;

    @Autowired
    private UserAuthenticationMasterRepo userAuthenticationMasterRepo;

    //Responsible For Scheduling Job
    public void scheduleDynamicReportJob(ReportPayload reportPayload) throws Exception {
        String randomUUID = UUID.randomUUID().toString();
        UserAuthDetails user_auth_details = authenticationUtility.getLoggedInUsername();
        int user_priority = 5;
        UserMaster userMaster = userAuthenticationMasterRepo.findByUserMastUsername(user_auth_details.getUsername());
        if(userMaster != null){
            user_priority = userMaster.getUserDesignation().getPriority();
        }
        String generatedUniqueJobName = reportPayload.getJobName() + "%" + randomUUID;
        JobDetail jobDetail = JobBuilder.newJob(ReportJob.class)
                .withIdentity(generatedUniqueJobName, "report-jobs")
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(generatedUniqueJobName + "_trigger", "dynamic-triggers")
                .withSchedule(CronScheduleBuilder.cronSchedule(reportPayload.getCronExpression()))
                .withPriority(user_priority)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        String formattedJobKey = CommonUtility.formatJobKey(
                jobDetail.getKey().getGroup() + "." + jobDetail.getKey().getName()
        );
        reportPayload.setJobKey(formattedJobKey);

        reportPayload.setUniqueJobName(jobDetail.getKey().toString());
        ReportPayload reportPayloadSaved = reportPayloadRepo.save(reportPayload);

        LocalDateTime scheduledFor = trigger.getNextFireTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        reportLogRepository.save(
                ReportLogsMaster.builder()
                        .jobKey(reportPayload.getJobName())
                        .reportName(generatedUniqueJobName)
                        .uniqueReportId(randomUUID)
                        .requestedAt(LocalDateTime.now())
                        .status(CommonUtility.REPORT_REQUESTED)
                        .scheduledFor(LocalDateTime.from(scheduledFor))
                        .requestedBy("Chetan Sawant")
                        .build()
        );
    }

    public void updateJobStatus( String updateType, String jobName ) throws SchedulerException{
        JobKey jobKey = new JobKey(jobName, "report-jobs");
        if(Objects.equals(updateType, "DELETE")){
            scheduler.pauseJob(jobKey);
        } else if (Objects.equals(updateType, "RESUME")) {
            scheduler.resumeJob(jobKey);
        } else if (Objects.equals(updateType, "PAUSE")) {
            scheduler.pauseJob(jobKey);
        }
    }

    public List<QuartzJobTriggerDTO> getScheduledJobs() {
        String sql = "SELECT j.SCHED_NAME, j.JOB_NAME, j.JOB_GROUP, j.DESCRIPTION AS JOB_DESCRIPTION, " +
                "j.JOB_CLASS_NAME, j.IS_DURABLE, j.IS_NONCONCURRENT, j.IS_UPDATE_DATA, " +
                "j.REQUESTS_RECOVERY, j.JOB_DATA, t.TRIGGER_NAME, t.TRIGGER_GROUP, " +
                "t.TRIGGER_STATE, t.PRIORITY, t.START_TIME, t.END_TIME, t.NEXT_FIRE_TIME, t.PREV_FIRE_TIME " +
                "FROM qrtz_job_details j " +
                "JOIN qrtz_triggers t ON j.SCHED_NAME = t.SCHED_NAME " +
                "AND j.JOB_NAME = t.JOB_NAME " +
                "AND j.JOB_GROUP = t.JOB_GROUP ";

        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> new QuartzJobTriggerDTO(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (String) row[4],
                (Boolean) row[5],
                (Boolean) row[6],
                (Boolean) row[7],
                (Boolean) row[8],
                (byte[]) row[9],
                (String) row[10],
                (String) row[11],
                (String) row[12],
                row[13] != null ? ((Number) row[13]).intValue() : null,
                row[14] != null ? ((Number) row[14]).longValue() : null,
                row[15] != null ? ((Number) row[15]).longValue() : null,
                row[16] != null ? ((Number) row[16]).longValue() : null,
                row[17] != null ? ((Number) row[17]).longValue() : null
        )).toList();
    }
}
