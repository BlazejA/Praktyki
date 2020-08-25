package com.hacktyki.car.LoginSignUp.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hacktyki.car.UserPanel.HomeActivity;
import com.hacktyki.car.R;


public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    EditText emailETxt, passwordETxt;
    FirebaseAuth firebaseAuth;
    SignUpPresenter signUpPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        emailETxt = findViewById(R.id.emailTxt);
        passwordETxt = findViewById(R.id.passwordTxt);

        signUpPresenter = new SignUpPresenter(this);

    }

    public void onClick(View v) {
        signUpPresenter.onButtonClick();
    }

    @Override
    public String getEmailETxt() {
        return emailETxt.getText().toString();
    }

    @Override
    public String getPasswordETxt() {
        return passwordETxt.getText().toString();
    }

    @Override
    public void fillAllFieldsMessage() {
        Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
    }

    boolean isSuccess;

    @Override
    public boolean isDataCorrect(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                isSuccess = task.isSuccessful();
            }
        });
        return isSuccess;
    }

    @Override
    public void loadNewActivity() {
        Toast.makeText(SignUpActivity.this, "Stworzono nowe konto!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(SignUpActivity.this, "Konto istnieje lub wpisano błędne dane!", Toast.LENGTH_SHORT).show();
    }
}
