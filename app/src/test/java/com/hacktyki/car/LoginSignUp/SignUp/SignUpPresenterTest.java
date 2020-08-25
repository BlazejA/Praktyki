package com.hacktyki.car.LoginSignUp.SignUp;

import org.junit.Assert;
import org.junit.Test;

public class SignUpPresenterTest {
    @Test
    public void shouldPass() {
        SignUpContract.View view = new MockView();

        SignUpPresenter presenter = new SignUpPresenter((SignUpContract.View) view);
        presenter.onButtonClick();

        Assert.assertEquals(true, ((SignUpPresenterTest.MockView) view).pass);
    }


    private class MockView implements SignUpContract.View {
        boolean pass;

        @Override
        public String getEmailETxt() {

            return "admin@gmail.com";
        }

        @Override
        public String getPasswordETxt() {
            return "adminadmin";
        }

        @Override
        public void fillAllFieldsMessage() {

        }

        @Override
        public boolean isDataCorrect(String email, String password) {

            return false;
        }

        @Override
        public void loadNewActivity() {
            pass = true;
        }

        @Override
        public void showErrorMessage() {

        }
    }
}