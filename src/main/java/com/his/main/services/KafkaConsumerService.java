package com.his.main.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.his.main.entities.mongo.ReportLogsMaster;
import com.his.main.repositories.mongo.ReportLogRepository;
import com.his.main.repositories.mongo.ReportPayloadRepo;
import org.his.core.dto.ReportGenerationPayloadDto;
import org.his.core.kafka.KafkaTopics;
import org.his.core.utiltiy.CommonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KafkaConsumerService {

    @Autowired
    private JavaMailSenderService javaMailSenderService;
    @Autowired
    private ReportPayloadRepo reportPayloadRepo;

    @Autowired
    private ReportServices reportServices;


    @KafkaListener(topics = "#{T(org.his.core.kafka.KafkaTopics).EMAIL_NOTIFICATION.getTopicName()}",groupId = "email-consumer-group")
    public void listenToEmailNotificationTopic(String message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ReportGenerationPayloadDto reportGenerationPayloadDto = objectMapper.readValue(message, ReportGenerationPayloadDto.class);
            String uniqueReportId = reportGenerationPayloadDto.getUniqueJobName().split("%")[1];
            Optional<ReportLogsMaster> reportLogsMaster = reportServices.findReportLogByReportName(uniqueReportId);
            javaMailSenderService.sendEmail("sawantchetan8149@gmail.com",reportLogsMaster.get().getUniqueReportId() );
            if(reportLogsMaster.isPresent()){
                this.saveReportLogsFromReportService(reportLogsMaster.get(),reportGenerationPayloadDto,CommonUtility.REPORT_DISPATCHED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "#{T(org.his.core.kafka.KafkaTopics).REPORT_STATUS.getTopicName()}",groupId = "report-status-group")
    public void listenToReportStatusTopic(String message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ReportGenerationPayloadDto reportGenerationPayloadDto = objectMapper.readValue(message, ReportGenerationPayloadDto.class);
            String uniqueReportId = reportGenerationPayloadDto.getUniqueJobName().split("%")[1];
            Optional<ReportLogsMaster> reportLogsMaster = reportServices.findReportLogByReportName(uniqueReportId);
            if(reportLogsMaster.isPresent()){
                this.saveReportLogsFromReportService(reportLogsMaster.get(),reportGenerationPayloadDto,CommonUtility.REPORT_COMPLETED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void saveReportLogsFromReportService(ReportLogsMaster reportLogsMasterObj,ReportGenerationPayloadDto reportGenerationPayloadDto,String status){

        reportLogsMasterObj.setStatus(CommonUtility.REPORT_DISPATCHED);
        reportLogsMasterObj.setCompletedAt(LocalDateTime.now());
        if(reportGenerationPayloadDto.isReportGenerationSucceed()){
            reportLogsMasterObj.setStatus(CommonUtility.REPORT_COMPLETED);
        }else{
            reportLogsMasterObj.setStatus(CommonUtility.REPORT_ERROR);
        }
        reportLogsMasterObj.setErrorMessage(reportGenerationPayloadDto.getErrorCausedBy());
        reportLogsMasterObj.setExecutionTimeMs(reportGenerationPayloadDto.getTimeTakenToGenerate());
        reportLogsMasterObj.setMemoryConsumedForGeneration(reportGenerationPayloadDto.getMemoryUsedUpToGenerate());
        reportLogsMasterObj.setGeneratedFilePath(reportGenerationPayloadDto.getReportFilePath());
        reportLogsMasterObj.setStatus(status);
        reportServices.saveReportLogsMaster(reportLogsMasterObj);
    }
}
