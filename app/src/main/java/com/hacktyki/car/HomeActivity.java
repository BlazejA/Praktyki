package com.hacktyki.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView userNameTxt;
    FirebaseRecyclerOptions<Cars> options;
    FirebaseRecyclerAdapter<Cars, MyViewHolder> adapter;
    DatabaseReference dbRef;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Cars");
        recyclerView = findViewById(R.id.myrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        LoadData();

        userNameTxt = (TextView) findViewById(R.id.userName);
        userNameTxt.setText(firebaseAuth.getCurrentUser().getEmail());
    }


    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<Cars>().setQuery(dbRef, Cars.class).build();
        adapter = new FirebaseRecyclerAdapter<Cars, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final Cars model) {
                holder.carName.setText(model.getCarMakes() + " " + model.getCarModel());
                Picasso.get().load(model.getImageUrl()).into(holder.carImage);

                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomeActivity.this, CurrentCarActivity.class);
                        i.putExtra("auto", model);
                        startActivity(i);
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

    public void onClick(View v) {
        startActivity(new Intent(HomeActivity.this, MyCarsActivity.class));
    }
}
