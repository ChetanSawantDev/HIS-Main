package com.his.main.services;

import com.his.main.entities.mongo.ReportLogsMaster;
import com.his.main.entities.mongo.ReportPayload;
import com.his.main.repositories.mongo.ReportLogRepository;
import com.his.main.repositories.mongo.ReportPayloadRepo;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.his.core.utiltiy.CommonUtility;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ExceptionClassifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServices {
    @Autowired
    private ReportLogRepository reportLogRepository;

    @Autowired
    private ReportPayloadRepo reportPayloadRepo;

    @Autowired
    private JobSchedulerService jobSchedulerService;

    public List<ReportLogsMaster> getAllReportLogs(){
        return reportLogRepository.findAll();
    }

    public void saveReportLogsMaster(ReportLogsMaster reportLogsMaster){
        reportLogRepository.save(reportLogsMaster);
    }

    public void saveReportPayload(ReportPayload reportPayload){
        reportPayloadRepo.save(reportPayload);
    }

    public List<ReportPayload> getAllReportPayload(){
        return reportPayloadRepo.findAll();
    }


    public Optional<ReportPayload> getReportPayloadForPrint(String jobId){
        return reportPayloadRepo.findByJobKey(jobId);
    }
}
