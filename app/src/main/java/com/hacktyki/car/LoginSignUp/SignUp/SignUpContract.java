package com.hacktyki.car.LoginSignUp.SignUp;

public class SignUpContract {
    public interface View{

        String getEmailETxt();

        String getPasswordETxt();

        void fillAllFieldsMessage();

        boolean isDataCorrect(String email, String password);

        void loadNewActivity();

        void showErrorMessage();
    }
}
