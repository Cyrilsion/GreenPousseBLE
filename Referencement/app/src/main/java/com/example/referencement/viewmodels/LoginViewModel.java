package com.example.referencement.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.referencement.R;
import com.example.referencement.login.Result;
import com.example.referencement.login.InscriptionActivity;
import com.example.referencement.login.LoggedInUserView;
import com.example.referencement.login.LoginFormState;
import com.example.referencement.login.LoginResult;
import com.example.referencement.models.LoginModel;
import com.example.referencement.repositories.LoginRepository;

public class LoginViewModel extends ViewModel {

    //private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginModel> loginModel = new MutableLiveData<>();
    //private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public void init() {
        if(loginModel == null) return;
        loginRepository = LoginRepository.getInstance();
        loginModel = loginRepository.getLoginModel();
    }


    public LiveData<LoginModel> getLoginResult() { return loginModel; }


    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password);
        /*Result<InscriptionActivity.LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            InscriptionActivity.LoggedInUser data = ((Result.Success<InscriptionActivity.LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
