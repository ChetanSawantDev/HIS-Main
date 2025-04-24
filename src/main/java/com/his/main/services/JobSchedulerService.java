package com.his.main.services;

import com.his.main.dto.quartzJob.ReportJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSchedulerService {
    @Autowired
    private Scheduler scheduler;

    public void scheduleDynamicJob(String jobName, String cronExpression) throws Exception {

        JobDetail jobDetail = JobBuilder.newJob(ReportJob.class)
                .withIdentity(jobName, "dynamic-jobs")
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "_trigger", "dynamic-triggers")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

    }
}
