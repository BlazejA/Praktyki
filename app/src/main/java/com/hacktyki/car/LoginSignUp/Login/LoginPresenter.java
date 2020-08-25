package com.hacktyki.car.LoginSignUp.Login;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

class LoginPresenter {
    private final LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }


    public void onButtonClick() {
        String email = view.getEmail();
        String password = view.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            view.fillAllFieldsMessage();
        } else {
            view.loginSuccessful();
        }
    }

    public void loginResult(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            view.delOldReservation();
            view.loadAccount();
        } else {
            view.wrongEmailOrPassword();
        }
    }
}
