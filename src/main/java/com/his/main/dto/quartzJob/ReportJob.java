package com.his.main.dto.quartzJob;

import org.his.core.kafka.KafkaTopics;
import org.his.core.kafka.services.KafkaProducerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportJob implements Job {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        kafkaProducerService.sendMessage(KafkaTopics.REPORT_GENERATION.getTopicName(), " This is new Report Payload ");
    }
}
