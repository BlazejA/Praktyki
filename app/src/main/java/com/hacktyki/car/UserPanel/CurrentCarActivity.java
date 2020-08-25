package com.hacktyki.car.UserPanel;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hacktyki.car.Model.BookedCars;
import com.hacktyki.car.Model.Cars;
import com.hacktyki.car.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CurrentCarActivity extends AppCompatActivity {

    private Cars car;
    private BookedCars bCar;
    private TextView datePick;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String data = "";
    public DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;
    private TextView carNameTxt;
    private EditText purposeEText;
    public List<BookedCars> carsList;
    private Button bookBtn;
    TextView userNameTxt;
    String visitPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_car);

        car = (Cars) getIntent().getSerializableExtra("auto");
        bCar = new BookedCars();
        dbRef = FirebaseDatabase.getInstance().getReference("Rezerwacje");
        datePick = (TextView) findViewById(R.id.dataTXT);
        firebaseAuth = FirebaseAuth.getInstance();
        carNameTxt = (TextView) findViewById(R.id.carName);
        carsList = new ArrayList();
        bookBtn = (Button) findViewById(R.id.rezBtn);
        userNameTxt = findViewById(R.id.userName);
        purposeEText = (EditText) findViewById(R.id.celTxt);

        userNameTxt.setText(firebaseAuth.getCurrentUser().getEmail());

        carNameTxt.setText(car.getCarMakes() + " " + car.getCarModel());
        addCarsToList();

        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);
                int d = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CurrentCarActivity.this, R.style.my_dialog_theme, dateSetListener, y, m, d);

                dialog.show();

            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                data = dayOfMonth + "." + month + "." + year;
                setDate();
            }
        };
    }

    public void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
        Date d = null;
        try {
            d = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("dd.MM.yyyy");
        String nowaData = sdf.format(d);
        data = nowaData;
        datePick.setText(data);
        isCarAvailable();
    }

    public String split(String str) {
        String[] arr = str.split("\\.");
        String nowy = "";
        for (String a : arr) {
            nowy += a;
        }
        return nowy;
    }

    public void addBookedCarToDb() {
        visitPurpose = purposeEText.getText().toString();
        if (!(data.isEmpty() || visitPurpose.isEmpty())) {
            bCar.setbCarMakes(car.getCarMakes());
            bCar.setbCarModel(car.getCarModel());
            bCar.setbCarData(data);
            bCar.setbCarUser(firebaseAuth.getCurrentUser().getEmail());
            bCar.setbCarImageUrl(car.getCarImageUrl());
            bCar.setbCarRegistrationNumber(car.getCarRegistrationNumber());
            String bookingId = bCar.getbCarRegistrationNumber() + bCar.getbCarUser().substring(0, bCar.getbCarUser().indexOf('@')) + split(bCar.getbCarData());
            bCar.setbCarId(bookingId);
            bCar.setbCarVisitPurpose(visitPurpose);
            dbRef.child(bookingId).setValue(bCar);
            Toast.makeText(this, "Auto zarezerwowane!", Toast.LENGTH_SHORT).show();
            addCarsToList();
        } else {
            Toast.makeText(this, "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View v) {
        addBookedCarToDb();

    }

    public void addCarsToList() {
        carsList.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookedCars _c = ds.getValue(BookedCars.class);
                    carsList.add(_c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        dbRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void isCarAvailable() {
        for (BookedCars bookedCar : carsList) {
            if (!(bookedCar.getbCarMakes().equals(car.getCarMakes()) && bookedCar.getbCarData().equals(data))) {
                bookBtn.setEnabled(true);
            } else {
                bookBtn.setEnabled(false);
                Toast.makeText(CurrentCarActivity.this, "Auto niedostępne w tym terminie!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
