package com.hacktyki.car;

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


public class SignUpActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailTxt);
        password = findViewById(R.id.passwordTxt);

    }

    public void onClick(View v) {
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        if (_email.isEmpty() || _password.isEmpty()) {
            Toast.makeText(this, "Wypełnij wszytkie pola!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Stworzono nowe konto!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Konto istnieje lub wpisano błędne dane!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
