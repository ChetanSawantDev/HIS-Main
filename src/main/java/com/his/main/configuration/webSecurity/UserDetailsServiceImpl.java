package com.his.main.configuration.webSecurity;

import com.his.main.authEntities.UserMaster;
import com.his.main.dto.UserAuthDetails;
import com.his.main.repositories.UserAuthenticationMasterRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthenticationMasterRepo userAuthenticationMasterRepo;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMaster userAuthenticationMaster = this.userAuthenticationMasterRepo.findByUserMastUsername(username);

        if(userAuthenticationMaster!=null){
            UserAuthDetails userAuthDetails = new UserAuthDetails(userAuthenticationMaster.getUserMastUsername(),userAuthenticationMaster.getUserMastPassword());
            System.out.println(userAuthDetails);
            return userAuthDetails;
        }
        return new UserAuthDetails();
    }
}
