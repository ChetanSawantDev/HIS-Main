package com.his.main.authEntities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "HIS_USER_AUTH_MASTER")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 255)
    private String userMastId;

    @Column(length = 35,nullable = false,unique = true)
    private String userMastUsername;

    @Column(length = 100)
    private String userMastPassword;

    @Column(unique = true)
    private String userDetailsId;

    @OneToOne
    @JoinColumn(columnDefinition = "userRoleId")
    private UserRoleMaster userMastRole;

    @Column(nullable = false)
    private boolean isActiveUser;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastLoggedInAt;
    
    private String companyId;

	public String getUserMastUsername() {
		return this.userMastUsername;
	}

	public String getUserMastPassword() {
		return this.userMastPassword;
	}
}
