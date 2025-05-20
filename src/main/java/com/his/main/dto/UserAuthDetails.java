package com.his.main.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.his.main.authEntities.UserMaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Builder
public class UserAuthDetails extends UserMaster implements UserDetails {

    private final UserMaster userMaster;

    public UserAuthDetails(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Example: return user roles if needed
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return userMaster.getUserMastPassword();
    }

    @Override
    public String getUsername() {
        return userMaster.getUserMastUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // or your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // or your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // or your logic
    }

    @Override
    public boolean isEnabled() {
        return userMaster.isActiveUser();
    }

    @Override
    public String toString() {
        return "UserAuthDetails{" +
                "username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
