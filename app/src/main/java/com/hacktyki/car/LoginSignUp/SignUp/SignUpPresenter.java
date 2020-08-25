package com.hacktyki.car.LoginSignUp.SignUp;

public class SignUpPresenter {

    private final SignUpContract.View view;

    SignUpPresenter(SignUpContract.View view) {
        this.view = view;
    }

    void onButtonClick() {
        String email = view.getEmailETxt();
        String password = view.getPasswordETxt();
        if (email.isEmpty() || password.isEmpty()) {
            fillAllFields();
        } else {
            if (view.isDataCorrect(email,password)) {
                loadNewActivity();
            } else {
                showError();
            }
        }
    }
    void fillAllFields(){
        view.fillAllFieldsMessage();
    }
    void loadNewActivity(){
        view.loadNewActivity();
    }
    void showError(){
        view.showErrorMessage();
    }


}

