package com.example.referencement.models;

public class LoginModel {

    private String userId;
    private String displayName;
    private boolean authentication;
    private Integer humidite;
    private Integer temperature;
    private Integer ph;
    private int error;
    private String mailadresss;

    public LoginModel() {
        this.authentication = false;
        this.error = 0;
    }

    public String getUserId() {
        return userId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public boolean isAuthenticated() { return authentication; }
    public void login(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.authentication = true;
        this.error = 0;
        this.mailadresss = "coucoudupond@plus";
    /// TODO : assigner les valeurs des capteurs
        this.humidite = 10;
        this.temperature = 20;
        this.ph = 7;
    }
    public void logout() {
        this.authentication = false;
    }
    public String getMailadresss() { return this.mailadresss; }
    public int getHumidite() {
        return humidite;
    }
    public int getPh() {
        return ph;
    }
    public int getTemperature() {
        return this.temperature;
    }
    public int getError() { return this.error; }
    public void setAdresseMail(String mail) { this.mailadresss = mail; }
    public void setError(int error) { this.error = error;}
}
