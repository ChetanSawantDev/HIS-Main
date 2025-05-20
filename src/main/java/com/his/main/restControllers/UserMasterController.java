package com.his.main.restControllers;

import com.his.main.authEntities.UserMaster;
import com.his.main.services.UserMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userMaster")
public class UserMasterController {
    @Autowired
    private UserMasterService userMasterService;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUserMaster(@RequestBody UserMaster userMaster){
        try{
            userMasterService.saveUserMaster(userMaster);
            return ResponseEntity.ok("Saved Successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
