package com.his.main.restControllers;

import com.his.main.services.JobSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduler")
public class JobSchedulerController {

    @Autowired
    private JobSchedulerService jobSchedulerService;
    @PostMapping("/create")
    public ResponseEntity<String> scheduleJob(@RequestParam String jobName,
                                              @RequestParam String cron){
        try{
            jobSchedulerService.scheduleDynamicJob(jobName,cron);
            return ResponseEntity.ok("Job Scheduled Successfully !");
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error : " + e.getMessage());
        }

    }
}
