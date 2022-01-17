package com.example.demo.securityConfig;

public enum ApplicationUserPermission {
	
	DELETE_USER("User:delete"),
	EDIT_USER("User:edit");
 
	private final String permission;
	
	ApplicationUserPermission(String permission) {
		this.permission = permission;
	}
	
	@SuppressWarnings("unused")
	private String getPermission() {
		return this.permission;
	}

}
