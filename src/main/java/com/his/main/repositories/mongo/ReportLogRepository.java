package com.his.main.repositories.mongo;

import com.his.main.entities.mongo.ReportLogsMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportLogRepository extends MongoRepository<ReportLogsMaster, String> {
    Optional<ReportLogsMaster> findByUniqueReportId(String reportName);
    List<ReportLogsMaster> findByRequestedBy(String user);

}