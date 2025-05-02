package com.his.main.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.his.main.entities.mongo.ReportPayload;
import com.his.main.services.JobSchedulerService;
import com.his.main.services.ReportServices;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportService")
public class ReportController {

    @Autowired
    private ReportServices reportServices;

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @PostMapping("/requestReport")
    public ResponseEntity<String> saveReportPayload(@RequestBody ReportPayload reportPayload){
        try{
            reportServices.saveReportPayload(reportPayload);
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Failed");
        }
    }

    @GetMapping("/getReportPayloads")
    public ResponseEntity<String> getAllReportPayload(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return ResponseEntity.ok(objectMapper.writeValueAsString(reportServices.getAllReportPayload()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getCause().toString());
        }
    }


    @PostMapping("scheduleReport")
    public ResponseEntity<String> scheduleReportForPrint(@RequestBody ReportPayload reportPayload){
        try{
            jobSchedulerService.scheduleDynamicReportJob(reportPayload);
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
