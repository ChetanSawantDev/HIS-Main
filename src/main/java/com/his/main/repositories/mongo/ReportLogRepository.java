package com.his.main.repositories.mongo;

import com.his.main.entities.mongo.ReportLogsMaster;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportLogRepository extends MongoRepository<ReportLogsMaster, String> {
    List<ReportLogsMaster> findByReportName(String reportName);
    List<ReportLogsMaster> findByRequestedBy(String user);
}