package com.his.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.his.main.authEntities.UserRoleMaster;

@Repository
public interface UserRoleMasterRepo extends JpaRepository<UserRoleMaster, String> {
}
