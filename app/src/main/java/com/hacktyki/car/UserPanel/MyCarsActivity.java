package com.hacktyki.car.UserPanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hacktyki.car.BaseClasses.BookedCars;
import com.hacktyki.car.BaseClasses.MyViewHolder;
import com.hacktyki.car.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyCarsActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView userNameTxt;
    FirebaseRecyclerOptions<BookedCars> options;
    FirebaseRecyclerAdapter<BookedCars, MyViewHolder> adapter;
    DatabaseReference dbRef;
    RecyclerView recyclerView;

    List<BookedCars> carsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);

        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Rezerwacje");
        carsList = new ArrayList();
        recyclerView = findViewById(R.id.myrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        addCarsToList();

        userNameTxt = (TextView) findViewById(R.id.userName);
        userNameTxt.setText(firebaseAuth.getCurrentUser().getEmail());
    }


    private void LoadData() {
        if (!carsList.isEmpty()) {
            options = new FirebaseRecyclerOptions.Builder<BookedCars>().setQuery(dbRef, BookedCars.class).build();
            adapter = new FirebaseRecyclerAdapter<BookedCars, MyViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final BookedCars model) {
                    if (model.getbCarUser().equals(firebaseAuth.getCurrentUser().getEmail())) {
                        holder.holderCarName.setText(model.getbCarMakes() + " " + model.getbCarModel() + "\n" + model.getbCarData());
                        Picasso.get().load(model.getbCarImageUrl()).into(holder.holderCarImage);
                        holder.holderCardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyCarsActivity.this);
                                builder.setTitle("ANULOWANIE REZERWACJI");
                                builder.setMessage("Czy na pewno chcesz anulować rezerwację?");
                                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbRef.child(model.getbCarId()).removeValue();
                                        Toast.makeText(MyCarsActivity.this, "Anulowano rezerwacje", Toast.LENGTH_SHORT).show();
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
                    } else {
                        hideReservation(holder);
                    }
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
        } else {
            Toast.makeText(MyCarsActivity.this, "Brak rezerwacji!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideReservation(MyViewHolder holder) {
        holder.holderCarImage.setVisibility(View.GONE);
        holder.holderCarName.setVisibility(View.GONE);
        holder.holderCardView.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.holderCardView.getLayoutParams();
        params.width = 0;
        params.leftMargin = 0;
        params.topMargin = 0;
        params.bottomMargin = 0;
        params.rightMargin = 0;
        params.height = 0;
        holder.holderCardView.setLayoutParams(params);
    }


    public void addCarsToList() {
        carsList.clear();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookedCars _c = ds.getValue(BookedCars.class);
                    if (_c.getbCarUser().equals(firebaseAuth.getCurrentUser().getEmail()))
                        carsList.add(_c);
                }
                LoadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
