package com.his.main.services;

import com.his.main.dto.quartzJob.ReportJob;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JobSchedulerService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private EntityManager entityManager;

    public void scheduleDynamicJob(String jobName, String cronExpression) throws Exception {

        JobDetail jobDetail = JobBuilder.newJob(ReportJob.class)
                .withIdentity(jobName, "dynamic-jobs")
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "_trigger", "dynamic-triggers")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .withPriority(5)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

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
