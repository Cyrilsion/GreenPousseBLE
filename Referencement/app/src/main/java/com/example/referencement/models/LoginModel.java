package com.example.referencement.models;

public class LoginModel {

    private String userId;
    private String displayName;
    private boolean authentication;

    public LoginModel(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.authentication = false;
    }

    public String getUserId() {
        return userId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public boolean isAuthenticated() { return authentication; }
    public void login() { this.authentication = true; }
}
