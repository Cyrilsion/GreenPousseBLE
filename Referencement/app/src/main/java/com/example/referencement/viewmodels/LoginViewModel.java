package com.example.referencement.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.referencement.models.LoginModel;
import com.example.referencement.repositories.LoginRepository;

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

    public int getError() {
        return loginModel.getValue().getError();
    }

}
