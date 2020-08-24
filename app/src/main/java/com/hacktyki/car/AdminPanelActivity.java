package com.hacktyki.car;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminPanelActivity extends AppCompatActivity {

    EditText modelET, markaET, registrationNumber;
    Button chooseBtn, addCarBtn;
    ImageView carImage;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    StorageReference storageRef;
    Cars newCar;
    TextView userNameTxt;
    Uri imguri;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        firebaseAuth = FirebaseAuth.getInstance();

        modelET = findViewById(R.id.modelTxt);
        markaET = findViewById(R.id.markaTxt);
        registrationNumber = findViewById(R.id.registrationNumber);
        addCarBtn = findViewById(R.id.addCarBtn);
        chooseBtn = findViewById(R.id.chooseBtn);
        carImage = findViewById(R.id.carImage);
        userNameTxt = findViewById(R.id.userName);

        dbRef = FirebaseDatabase.getInstance().getReference("Cars");
        storageRef = FirebaseStorage.getInstance().getReference("Images");
        newCar = new Cars();

        userNameTxt.setText(firebaseAuth.getCurrentUser().getEmail());

        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUploader();
            }
        });
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });
    }

    private void addCarToDb() {
        String _model = modelET.getText().toString();
        String _marka = markaET.getText().toString();
        String _registrationNumber = registrationNumber.getText().toString();
        if (_model.isEmpty() || _marka.isEmpty() || _registrationNumber.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
        } else {
            newCar.setCarMakes(_marka);
            newCar.setCarModel(_model);
            newCar.setRegistrationNumber(_registrationNumber);
            newCar.setImageUrl(downloadUrl);
            dbRef.child(newCar.registrationNumber).setValue(newCar);
            Toast.makeText(AdminPanelActivity.this, "Dodano nowe auto!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void fileUploader() {
        final String key = dbRef.push().getKey();
        if (imguri != null) {
            storageRef.child(key + ".jpg").putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            addCarToDb();
                        }
                    });

                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AdminPanelActivity.this, "Wystąpił błąd!", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(AdminPanelActivity.this, "Dodaj zdjęcie!", Toast.LENGTH_SHORT).show();
        }

    }

    private void fileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();
            carImage.setImageURI(imguri);
        }
    }
}
