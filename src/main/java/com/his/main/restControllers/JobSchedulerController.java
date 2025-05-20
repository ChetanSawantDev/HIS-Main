package com.his.main.restControllers;

import com.his.main.entities.mongo.ReportPayload;
import com.his.main.services.JobSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler")
public class JobSchedulerController {

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @GetMapping("/update")
    public ResponseEntity<String> updateScheduledJobStatus(@RequestParam String updateType,
                                                           @RequestParam  String jobName){
        try {
            jobSchedulerService.updateJobStatus(updateType, jobName);
            return ResponseEntity.ok("Scheduled Job " + updateType.toLowerCase() + " successfully !");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error : " + e.getMessage());
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
