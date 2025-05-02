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

@Service
public class KafkaConsumerService {

    @Autowired
    private JavaMailSenderService javaMailSenderService;
    @Autowired
    private ReportPayloadRepo reportPayloadRepo;

    @Autowired
    private ReportLogRepository reportLogRepository;

    @KafkaListener(topics = "#{T(org.his.core.kafka.KafkaTopics).EMAIL_NOTIFICATION.getTopicName()}",groupId = "email-consumer-group")
    public void listenToEmailNotificationTopic(String message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ReportGenerationPayloadDto reportGenerationPayloadDto = objectMapper.readValue(message, ReportGenerationPayloadDto.class);
            javaMailSenderService.sendEmail("sawantchetan8149@gmail.com",reportGenerationPayloadDto.getUniqueJobName() );
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Received message: " + message);
    }

    @KafkaListener(topics = "#{T(org.his.core.kafka.KafkaTopics).REPORT_STATUS.getTopicName()}",groupId = "report-status-group")
    public void listenToReportStatusTopic(String message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
//            ReportGenerationPayloadDto reportGenerationPayloadDto = objectMapper.readValue(message, ReportGenerationPayloadDto.class);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
