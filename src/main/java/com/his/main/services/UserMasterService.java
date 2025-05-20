package com.his.main.services;

import com.his.main.authEntities.UserMaster;
import com.his.main.repositories.UserAuthenticationMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMasterService {
    @Autowired
    private UserAuthenticationMasterRepo userAuthenticationMasterRepo;

    public void saveUserMaster(UserMaster userMaster){
        userAuthenticationMasterRepo.save(userMaster);
    }
}
