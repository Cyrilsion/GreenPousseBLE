package com.example.greenpousse.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenpousse.models.LoginModel;
import com.example.greenpousse.repositories.LoginRepository;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginModel> loginModel = new MutableLiveData<>();
    private LoginModel login;
    private LoginRepository loginRepository;

    public void init() {
        if(loginModel == null) return;
        loginRepository = LoginRepository.getInstance();
        loginModel = loginRepository.getLoginModel();
    }

    public LiveData<LoginModel> getLoginResult() { return loginModel; }

    public void login(String username, String password, boolean knonw) {
        if(knonw) {
            login = loginModel.getValue();
            login.login(username, password);
            loginModel.setValue(login);
        }
        //si pas connu on va check la base de données
        else loginRepository.login(username, password);

    }

    public void logout() {
        loginModel.getValue().logout();
    }

    public void setDevice(String deviceName, String deviceAdress) {
        login = loginModel.getValue();
        login.setDevice(deviceName, deviceAdress);
        loginModel.setValue(login);
    }

    public void setCapteurs(int humidite, int ph, int temperature) {
        login = loginModel.getValue();
        login.setCapteurs(humidite, ph, temperature);
        loginModel.setValue(login);
    }

    public void getConseils(int temperature, int humidite, int ph) { loginRepository.getConseils(temperature, humidite, ph); }

    public int getError() {
        return loginModel.getValue().getError();
    }

}
