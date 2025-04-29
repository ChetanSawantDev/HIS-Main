package com.his.main.repositories.mongo;

import com.his.main.entities.mongo.ReportPayload;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportPayloadRepo  extends MongoRepository<ReportPayload, String> {
}
