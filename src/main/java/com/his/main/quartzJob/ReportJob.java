package com.his.main.quartzJob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.his.main.entities.mongo.ReportPayload;

import com.his.main.services.ReportServices;
import org.his.core.kafka.KafkaTopics;
import org.his.core.kafka.services.KafkaProducerService;
import org.his.core.utiltiy.CommonUtility;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ReportJob implements Job {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ReportServices reportServices;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //Responsible For Executing Scheduled Job
        String formattedJobKey = CommonUtility.formatJobKey(jobExecutionContext.getJobDetail().getKey().toString());
        Optional<ReportPayload> reportPayload = reportServices.getReportPayloadForPrint(formattedJobKey);
        ObjectMapper objectMapper = new ObjectMapper();
        String reportPayloadJson = "";
        try{
            if(reportPayload.isPresent()){
                reportPayloadJson = objectMapper.writeValueAsString(reportPayload.get());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        kafkaProducerService.sendMessage(KafkaTopics.REPORT_GENERATION.getTopicName(), reportPayloadJson);
    }
}
