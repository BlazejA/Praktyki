package com.hacktyki.car.LoginSignUp.Login;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginPresenterTest {

    @Test
    public void isDataTypeCorrectly() {
        LoginContract.View view = new MockView();

        LoginPresenter presenter = new LoginPresenter((LoginContract.View) view);
        presenter.onButtonClick();

        Assert.assertEquals(true, ((MockView) view).pass);
    }


    private class MockView implements LoginContract.View {
        boolean pass;

        @Override
        public String getEmail() {
            return "admin@gmail.com";
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public void fillAllFieldsMessage() {

        }

        @Override
        public void loginSuccessful() {
            pass = true;
        }

        @Override
        public void delOldReservation() {

        }

        @Override
        public void loadAccount() {
        }

        @Override
        public void wrongEmailOrPassword() {

        }
    }
}