package com.his.main.services;

import com.his.main.quartzJob.ReportJob;
import com.his.main.entities.mongo.ReportLogsMaster;
import com.his.main.entities.mongo.ReportPayload;
import com.his.main.repositories.mongo.ReportLogRepository;
import com.his.main.repositories.mongo.ReportPayloadRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.his.core.utiltiy.CommonUtility;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    //Responsible For Scheduling Job
    public void scheduleDynamicReportJob(ReportPayload reportPayload) throws Exception {
        String generatedUniqueJobName = reportPayload.getJobName() + "%" + UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(ReportJob.class)
                .withIdentity(generatedUniqueJobName, "dynamic-jobs")
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(generatedUniqueJobName + "_trigger", "dynamic-triggers")
                .withSchedule(CronScheduleBuilder.cronSchedule(reportPayload.getCronExpression()))
                .withPriority(5)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        String formattedJobKey = CommonUtility.formatJobKey(
                jobDetail.getKey().getGroup() + "." + jobDetail.getKey().getName()
        );
        reportPayload.setJobKey(formattedJobKey);
        reportPayload.setUniqueJobName(jobDetail.getKey() + UUID.randomUUID().toString());
        ReportPayload reportPayloadSaved = reportPayloadRepo.save(reportPayload);

        reportLogRepository.save(
                ReportLogsMaster.builder()
                        .jobKey(reportPayload.getJobName())
                        .reportName(generatedUniqueJobName)
                        .status(CommonUtility.REPORT_REQUESTED)
                        .requestedBy("User")
                        .build()
        );
    }

    public void updateJobStatus( String updateType, String jobName ) throws SchedulerException{
        JobKey jobKey = new JobKey(jobName, "dynamic-jobs");
        if(Objects.equals(updateType, "DELETE")){
            scheduler.pauseJob(jobKey);
        } else if (Objects.equals(updateType, "RESUME")) {
            scheduler.resumeJob(jobKey);
        } else if (Objects.equals(updateType, "PAUSE")) {
            scheduler.pauseJob(jobKey);
        }
    }

    public void getScheduledJobs() {
        String sql = "SELECT j.SCHED_NAME, j.JOB_NAME, j.JOB_GROUP, j.DESCRIPTION AS JOB_DESCRIPTION, " +
                "j.JOB_CLASS_NAME, j.IS_DURABLE, j.IS_NONCONCURRENT, j.IS_UPDATE_DATA, " +
                "j.REQUESTS_RECOVERY, j.JOB_DATA, t.TRIGGER_NAME, t.TRIGGER_GROUP, " +
                "t.TRIGGER_STATE, t.PRIORITY, t.START_TIME, t.END_TIME, t.NEXT_FIRE_TIME, t.PREV_FIRE_TIME " +
                "FROM qrtz_job_details j " +
                "JOIN qrtz_triggers t ON j.SCHED_NAME = t.SCHED_NAME " +
                "AND j.JOB_NAME = t.JOB_NAME " +
                "AND j.JOB_GROUP = t.JOB_GROUP ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("schedName", "someSchedName");

        List<Object[]> results = query.getResultList();
    }
}
