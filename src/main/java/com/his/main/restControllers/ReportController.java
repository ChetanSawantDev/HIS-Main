package com.his.main.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.his.main.entities.mongo.ReportLogsMaster;
import com.his.main.entities.mongo.ReportPayload;
import com.his.main.services.JobSchedulerService;
import com.his.main.services.ReportServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;

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

    @GetMapping("allReportLogs")
    public ResponseEntity<?> getAllReportLogs(){
        try{
            return ResponseEntity.ok(reportServices.getAllReportLogs());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
    @GetMapping("reportLogByJobName")
    public ResponseEntity<?> findReportLogByReportName(@RequestParam String jobName){
        try{
            return ResponseEntity.ok(reportServices.findReportLogByReportName(jobName).get());
        }catch (Exception e){
            return ResponseEntity.status(500).body("Server Error");
        }

    }

    @GetMapping("/printReport")
    public ResponseEntity<?> getReportPrint(@RequestParam String reportId){
        try {
            Optional<ReportLogsMaster> reportLogsMasterOptional = reportServices.findReportLogByReportName(reportId);

            if (reportLogsMasterOptional.isPresent()) {
                String filePath = reportLogsMasterOptional.get().getGeneratedFilePath();

                File file = new File(filePath);
                if (!file.exists()) {
                    return ResponseEntity.status(404).body("File not found");
                }

                FileSystemResource resource = new FileSystemResource(file);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                        .contentType(MediaType.APPLICATION_PDF)
                        .contentLength(file.length())
                        .body(resource);
            } else {
                return ResponseEntity.status(404).body("Report not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error while fetching report");
        }
    }
}
