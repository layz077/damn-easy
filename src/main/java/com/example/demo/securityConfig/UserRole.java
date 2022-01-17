package com.example.demo.securityConfig;

import java.util.Set;

import com.google.common.collect.Sets;

public enum UserRole {
	
	ADMIN(Sets.newHashSet(ApplicationUserPermission.EDIT_USER,ApplicationUserPermission.DELETE_USER)),
	USER(Sets.newHashSet());
	
	private Set<ApplicationUserPermission> permissions;
	
	UserRole(Set<ApplicationUserPermission> permissions){
		this.permissions = permissions;
	}
	
    
}
