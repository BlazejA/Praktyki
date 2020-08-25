package com.hacktyki.car.LoginSignUp.Login;

class LoginContract {
    public interface View{

        String getEmail();

        String getPassword();

        void fillAllFieldsMessage();

        void loginSuccessful();

        void delOldReservation();

        void loadAccount();

        void wrongEmailOrPassword();
    }
}
