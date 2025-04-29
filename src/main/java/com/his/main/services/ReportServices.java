package com.his.main.services;

import com.his.main.entities.mongo.ReportLogsMaster;
import com.his.main.entities.mongo.ReportPayload;
import com.his.main.repositories.mongo.ReportLogRepository;
import com.his.main.repositories.mongo.ReportPayloadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServices {
    @Autowired
    private ReportLogRepository reportLogRepository;

    @Autowired
    private ReportPayloadRepo reportPayloadRepo;

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
}
