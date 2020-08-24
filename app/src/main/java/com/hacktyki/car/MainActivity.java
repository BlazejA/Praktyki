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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText emailETxt, passwordETxt;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef;
    List<BookedCars> carsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbRef = FirebaseDatabase.getInstance().getReference("Rezerwacje");
        firebaseAuth = FirebaseAuth.getInstance();
        emailETxt = findViewById(R.id.emailTxt);
        passwordETxt = findViewById(R.id.passwordTxt);
        carsList = new ArrayList();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                loginSystem();
                break;
            case R.id.registerBtn:
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
                break;

        }

    }

    public void loginSystem() {
        String _email = emailETxt.getText().toString();
        String _password = passwordETxt.getText().toString();
        if (_email.isEmpty() || _password.isEmpty()) {
            Toast.makeText(this, "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        delOldReservation();
                        Intent userPanel = new Intent(MainActivity.this, HomeActivity.class);
                        Intent adminPanel = new Intent(MainActivity.this, AdminPanelMainAcitivity.class);

                        if (firebaseAuth.getCurrentUser().getUid().equals("cDdyfEqFZJg3keYvXfY8enwMpey1")) {
                            startActivity(adminPanel);
                        } else {
                            startActivity(userPanel);
                        }
                        Toast.makeText(MainActivity.this, "Zalogowano!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Błędny email lub hasło!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = sdf.format(cal.getTime());
        return yesterday;
    }

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

}
