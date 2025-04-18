package com.his.main.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.his.main.authEntities.UserMaster;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAuthenticationMasterRepo extends JpaRepository<UserMaster, String> {
    UserMaster findByUserMastUsername(String userMastUsername);

}
