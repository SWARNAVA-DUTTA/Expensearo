package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensearo.JavaClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class Registration extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText user_name,user_email,user_password,user_confirm_password;
    ProgressDialog progressDialog;
    LinearLayout register_button;
    DatabaseReference myRef;
    TextView login_txt;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        user_name=(EditText)findViewById(R.id.user_name);
        user_email=(EditText)findViewById(R.id.user_email);
        user_password=(EditText)findViewById(R.id.user_password);
        user_confirm_password=(EditText)findViewById(R.id.user_confirm_password);
        register_button=findViewById(R.id.register_button);
        login_txt=findViewById(R.id.login_txt);
        progressDialog=new ProgressDialog(this);
        myRef= FirebaseDatabase.getInstance().getReference().child("Users");
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LogIn.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser(){

        final String name = user_name.getText().toString().trim();
        final String email = user_email.getText().toString().trim();
        final String password = user_password.getText().toString().trim();
        String confirm_password  = user_confirm_password.getText().toString().trim();


        if(TextUtils.isEmpty(name)){
            user_name.setError("Please enter your name");
            return;
        }
        else if(TextUtils.isEmpty(email)){
            user_email.setError("Please enter your email");
            return;
        }
        else if(!email.matches(emailPattern))
        {
            user_email.setError("Please enter a valid email");
            return;
        }
        else if(TextUtils.isEmpty(password)){
            user_password.setError("Please enter your password");
            return;
        }

        else if(TextUtils.isEmpty(confirm_password)){
            user_confirm_password.setError("Please enter your password");
            return;
        }
        else if(!password.equals(confirm_password))
        {
            user_password.setError("Passwords do not match");
            user_confirm_password.setError("Passwords do not match");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        else {
            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();

            //creating a new user

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                User user = new User();
                                user.setName(name);
                                user.setEmail(email);
                                user.setProfile_picture("https://firebasestorage.googleapis.com/v0/b/hungerfill-74ea1.appspot.com/o/Profile%20Pictures%2Fblank_profile.png?alt=media&token=d9d07f2d-fb92-4988-a149-e2e397f72656");
//                                user.setPassword(password);
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                myRef.child(currentUserId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Registration.this, "Successfully registered", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), LogIn.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.fade_in,
                                                R.anim.fade_out);
                                    }
                                });

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

}