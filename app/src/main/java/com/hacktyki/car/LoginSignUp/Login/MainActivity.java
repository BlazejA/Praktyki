package com.hacktyki.car.LoginSignUp.Login;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hacktyki.car.AdminPanel.AdminPanelMainAcitivity;
import com.hacktyki.car.LoginSignUp.SignUp.SignUpActivity;
import com.hacktyki.car.UserPanel.HomeActivity;
import com.hacktyki.car.Model.BookedCars;
import com.hacktyki.car.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginContract.View {

    EditText emailETxt, passwordETxt;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef;
    List<BookedCars> carsList;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbRef = FirebaseDatabase.getInstance().getReference("Rezerwacje");
        firebaseAuth = FirebaseAuth.getInstance();
        emailETxt = findViewById(R.id.emailTxt);
        passwordETxt = findViewById(R.id.passwordTxt);
        carsList = new ArrayList();
        loginPresenter = new LoginPresenter(this);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                loginPresenter.onButtonClick();
                break;
            case R.id.registerBtn:
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void delOldReservation() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookedCars _tempbCar = ds.getValue(BookedCars.class);
                    carsList.add(_tempbCar);
                }
                for (BookedCars bookedCar : carsList) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
                        Date date1 = sdf.parse(getYesterdayDate());
                        Date date2 = sdf.parse(bookedCar.getbCarData());
                        if (date1.compareTo(date2) >= 0) {
                            dbRef.child(bookedCar.getbCarId()).removeValue();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        dbRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void loadAccount() {
        Intent userPanel = new Intent(MainActivity.this, HomeActivity.class);
        Intent adminPanel = new Intent(MainActivity.this, AdminPanelMainAcitivity.class);
        if (firebaseAuth.getCurrentUser().getUid().equals("cDdyfEqFZJg3keYvXfY8enwMpey1")) {
            startActivity(adminPanel);
        } else {
            startActivity(userPanel);
        }
        Toast.makeText(this, "Zalogowano!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void wrongEmailOrPassword() {
        Toast.makeText(MainActivity.this, "Błędny email lub hasło!", Toast.LENGTH_SHORT).show();
    }

    public String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = sdf.format(cal.getTime());
        return yesterday;
    }

    @Override
    public String getEmail() {
        return emailETxt.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordETxt.getText().toString();
    }

    @Override
    public void fillAllFieldsMessage() {
        Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccessful() {
        firebaseAuth.signInWithEmailAndPassword(getEmail(), getPassword()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginPresenter.loginResult(task);
            }
        });
    }
}
