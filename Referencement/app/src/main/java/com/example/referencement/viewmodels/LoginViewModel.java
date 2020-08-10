package com.example.referencement.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.referencement.R;
import com.example.referencement.models.LoginModel;
import com.example.referencement.repositories.LoginRepository;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginModel> loginModel = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public void init() {
        if(loginModel == null) return;
        loginRepository = LoginRepository.getInstance();
        loginModel = loginRepository.getLoginModel();
    }

    public LiveData<LoginModel> getLoginResult() { return loginModel; }

    public void login(String username, String password) {
        loginRepository.login(username, password);
    }

    public void logout() {
        loginRepository.logout();
    }

    public int getError() {
        if (loginModel.getValue().getUserId().equalsIgnoreCase("Adresse email invalide")) return R.string.toast_emailErreur;
        else if (loginModel.getValue().getUserId().equalsIgnoreCase("Mot de passe invalide")) return R.string.toast_mdpErreur;
        else return R.string.snackbar_pbCoetc;
    }

}
