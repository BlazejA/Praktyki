package com.hacktyki.car;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminPanelMainAcitivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView userNameTxt;
    FirebaseRecyclerOptions<Cars> options;
    FirebaseRecyclerAdapter<Cars, MyViewHolder> adapter;
    DatabaseReference dbRef;
    RecyclerView recyclerView;
    Button addCarActivityBtn, allReservationBtn;
    List<BookedCars> carIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_main_acitivity);
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Cars");
        recyclerView = findViewById(R.id.myrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        loadData();

        userNameTxt = (TextView) findViewById(R.id.userName);
        userNameTxt.setText(firebaseAuth.getCurrentUser().getEmail());
        addCarActivityBtn = findViewById(R.id.addCar);
        allReservationBtn = findViewById(R.id.allReservationBtn);
        carIdList = new ArrayList<BookedCars>();

        addCarActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPanelMainAcitivity.this, AdminPanelActivity.class);
                startActivity(i);
            }
        });

        allReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPanelMainAcitivity.this, AdminPanelAllReservation.class);
                startActivity(i);
            }
        });

    }

    private void loadData() {
        options = new FirebaseRecyclerOptions.Builder<Cars>().setQuery(dbRef, Cars.class).build();
        adapter = new FirebaseRecyclerAdapter<Cars, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final Cars model) {
                holder.carName.setText(model.getCarMakes() + " " + model.getCarModel() + "\n" + model.getRegistrationNumber());
                Picasso.get().load(model.getImageUrl()).into(holder.carImage);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelMainAcitivity.this);
                        builder.setTitle("USUWANIE SAMOCHODU Z BAZY");
                        builder.setMessage("Czy na pewno chcesz usunąć samochód z bazy?");
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeValue(model);
                            }
                        });
                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                });
            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    public void removeValue(final Cars model) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Cars");
        dbRef.child(model.getRegistrationNumber()).removeValue();
        final DatabaseReference dbRefRez = FirebaseDatabase.getInstance().getReference("Rezerwacje");
        dbRefRez.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carIdList.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    carIdList.add(x.getValue(BookedCars.class));
                }
                for (BookedCars x : carIdList) {
                    if (x.getbCarId().contains(model.getRegistrationNumber())) {
                        dbRefRez.child(x.getbCarId()).removeValue();
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(x.getbCarImageUrl());
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminPanelMainAcitivity.this, "Usunięto samochód z bazy", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminPanelMainAcitivity.this, "Błąd!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
