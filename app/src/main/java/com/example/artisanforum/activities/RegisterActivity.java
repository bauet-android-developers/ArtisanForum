package com.example.artisanforum.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artisanforum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, fullname, phonenumber, email, password, location;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    String[] division = {"Barisal", "Chittagong", "Dhaka", "Khulna", "Mymensingh", "Rajshahi"
            , "Rangpur", "Sylhet"};
    String[] barishal = {"Barguna ", "Barisal", "Bhola", "Jhalokati", "Patuakhali", "Pirojpur"};
    String[] chittagong = {"Bandarban", "Brahmanbaria", "Chandpur", "Chittagong", "Comilla",
            "Cox's Bazar", "Feni", "Khagrachhari", "Lakshmipur",
            "Noakhali", "Rangamati"};
    String[] dhaka = {"Dhaka", "Faridpur", "Gazipur", "Gopalganj", "Kishoreganj", "Madaripur",
            "Manikganj", "Munshiganj", "Narayanganj", "Narsingdi", "Rajbari", "Shariatpur",
            "Tangail"};
    String[] khulna = {"Bagerhat", "Chuadanga", "Jessore", "Jhenaidah", "Khulna", "Kushtia",
            "Magura", "Meherpur", "Narail", "Satkhira"};
    String[] Mymensingh = {"Jamalpur", "Mymensingh", "Netrokona", "Sherpur"};
    String[] rajshahi = {"Bogra", "Joypurhat", "Naogaon", "Natore", "Chapainawabganj",
            "Pabna", "Rajshahi", "Sirajganj"};
    String[] rangpur = {"Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari",
            "Panchagarh", "Rangpur", "Thakurgaon"};
    String[] shylet = {"Habiganj", "Moulvibazar", "Sunamganj", "Sylhet"};
    String selecteddiv, selecteddis, name, phone;
    MaterialBetterSpinner district_;
    EditText name_, phone_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);;
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);
//        location = findViewById(R.id.location);
        name_ = findViewById(R.id.fullname);
        phone_ = findViewById(R.id.phonenumber);
        MaterialBetterSpinner division_ = findViewById(R.id.division);
        district_ = findViewById(R.id.district);

        auth = FirebaseAuth.getInstance();
        ArrayAdapter<String> typearrayAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, division);
        division_.setAdapter(typearrayAdapter);
        division_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecteddiv = parent.getItemAtPosition(position).toString();
                district_.setVisibility(View.VISIBLE);
                switch (selecteddiv) {
                    case "Barisal":
                        selectingDistrict(barishal);
                        break;
                    case "Chittagong":
                        selectingDistrict(chittagong);
                        break;
                    case "Dhaka":
                        selectingDistrict(dhaka);
                        break;
                    case "Khulna":
                        selectingDistrict(khulna);
                        break;
                    case "Mymensingh":
                        selectingDistrict(Mymensingh);
                        break;
                    case "Rajshahi":
                        selectingDistrict(rajshahi);
                        break;
                    case "Rangpur":
                        selectingDistrict(rangpur);
                        break;
                    case "Sylhet":
                        selectingDistrict(shylet);
                        break;
                }
            }
        });
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

//                name = name_.getText().toString();
//                phone = phone_.getText().toString();
                String str_username = username.getText().toString();
                String str_fullname = name_.getText().toString();
                String str_phonenumber = phone_.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();
//                String str_location = location.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_phonenumber) || TextUtils.isEmpty(selecteddis)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    register(str_username, str_fullname, str_email, str_password, str_phonenumber, selecteddis);
                }
            }
        });
    }

    public void register(final String username, final String fullname, final String email, String password, final String phonenumber, final String location) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("username", username.toLowerCase());
                            map.put("name", fullname);
                            map.put("email", email);
                            map.put("phone", phonenumber);
                            map.put("district", selecteddis);
//                            map.put("location", location);
                            map.put("bio", "");

                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Users");
                                        reference.child("name").setValue(name);
                                        reference.child("mobile").setValue(phone);
                                        reference.child("district").setValue(selecteddis);
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, OwnerActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void selectingDistrict(String[] x) {
        ArrayAdapter<String> typeDistrict = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, x);
        district_.setAdapter(typeDistrict);
        district_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecteddis = parent.getItemAtPosition(position).toString();
            }
        });

    }
}
