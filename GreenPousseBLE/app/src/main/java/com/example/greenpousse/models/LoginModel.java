package com.example.greenpousse.models;

public class LoginModel {

    private String userId;
    private String mail;
    private String displayName;
    private Integer humidite;
    private Integer temperature;
    private Integer ph;
    private String conseilTemperature;
    private String conseilHumidite;
    private String conseilPh;
    private int error;
    private String deviceName;
    private String deviceAdress;

    private boolean authentication;
    private boolean dataValid;

    public LoginModel() {
        this.authentication = false;
        this.error = 0;
        this.dataValid = false;
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
    }
    public void logout() {
        this.authentication = false;
    }
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
    public String getDeviceName() { return this.deviceName; }
    public String getDeviceAdress() { return this.deviceAdress; }
    public void setMail(String mail) { this.mail = mail; }
    public String getMail() { return this.mail; }
    public void setError(int error) { this.error = error;}
    public void setDevice(String deviceName, String deviceAdress) {
        this.deviceName = deviceName;
        this.deviceAdress = deviceAdress;
    }
    public void setCapteurs(int humidite, int ph, int temperature) {
        this.temperature = temperature;
        this.humidite = humidite;
        this.ph = 7;
        this.dataValid = true;
    }
    public void setConseils(String conseilTemperature, String conseilHumidite, String conseilPh) {
        this.conseilTemperature = conseilTemperature;
        this.conseilHumidite = conseilHumidite;
        this.conseilPh = conseilPh;
    }
    public String getConseilTemperature() { return this.conseilTemperature; }
    public String getConseilHumidite() { return this.conseilHumidite; }
    public String getConseilPh() { return this.conseilPh; }
    public boolean isDataValid() {
        return this.dataValid;
    }
}
