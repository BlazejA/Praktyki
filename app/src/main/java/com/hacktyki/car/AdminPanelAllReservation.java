package com.hacktyki.car;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminPanelAllReservation extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView userNameTxt;
    FirebaseRecyclerOptions<BookedCars> options;
    FirebaseRecyclerAdapter<BookedCars, MyViewHolder> adapter;
    DatabaseReference dbRef;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);

        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Rezerwacje");
        recyclerView = findViewById(R.id.myrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadData();

        userNameTxt = (TextView) findViewById(R.id.userName);
        userNameTxt.setText(firebaseAuth.getCurrentUser().getEmail());
    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<BookedCars>().setQuery(dbRef, BookedCars.class).build();
        adapter = new FirebaseRecyclerAdapter<BookedCars, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final BookedCars model) {
                holder.carName.setText(model.getbCarMakes() + " " + model.getbCarModel() + "\n" + model.getbCarData() + "\n" + model.getbCarUser() + " - " + model.getbCarVisitPurpose());
                Picasso.get().load(model.getbCarImageUrl()).into(holder.carImage);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelAllReservation.this);
                        builder.setTitle("ANULOWANIE REZERWACJI");
                        builder.setMessage("Czy na pewno chcesz anulować rezerwację?");
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dbRef.child(model.getbCarId()).removeValue();
                                Toast.makeText(AdminPanelAllReservation.this, "Anulowano rezerwacje", Toast.LENGTH_SHORT).show();
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
}
