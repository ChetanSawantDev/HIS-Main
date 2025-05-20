package com.his.main.services;

import com.his.main.dto.QuartzJobTriggerDTO;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServices {
    @Autowired
    private ReportLogRepository reportLogRepository;

    @Autowired
    private ReportPayloadRepo reportPayloadRepo;

    @Autowired
    private JobSchedulerService JobSchedulerService;

    public List<ReportLogsMaster> getAllReportLogs(){

        List<ReportLogsMaster> reportLogsMasterList = reportLogRepository.findAll();
        List<QuartzJobTriggerDTO> jobSchedulerList = JobSchedulerService.getScheduledJobs();
        reportLogsMasterList = reportLogsMasterList.stream()
                .map(l_report_log -> {
                    Optional<QuartzJobTriggerDTO> quartzJobTriggerDTO = jobSchedulerList.stream()
                            .filter(l_job_scheduler -> {
                                return Objects.equals(l_job_scheduler.getTriggerName().split("_")[0], l_report_log.getReportName());
                            })
                            .findFirst();

                    quartzJobTriggerDTO.ifPresent(dto ->
                            l_report_log.setSchedulerStatus(dto.getTriggerState())
                    );

                    return l_report_log;
                })
                .collect(Collectors.toList());

        return reportLogsMasterList;
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

    public Optional<ReportLogsMaster> findReportLogByReportName(String reportName){
        return reportLogRepository.findByUniqueReportId(reportName);
    }

}
