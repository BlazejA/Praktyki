package com.hacktyki.car.LoginSignUp.SignUp;

public class SignUpPresenter {

    private final SignUpContract.View view;

    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
    }

    public void onButtonClick() {
        String email = view.getEmailETxt();
        String password = view.getPasswordETxt();
        if (email.isEmpty() || password.isEmpty()) {
            view.fillAllFieldsMessage();
        } else {
            if (view.isDataCorrect(email,password)) {
                view.loadNewActivity();
            } else {
                view.showErrorMessage();
            }
        }
    }
}

