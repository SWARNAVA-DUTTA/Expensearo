package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SendEmailActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button send;
    EditText email;
    LinearLayout  view;
    Animation animShake;
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        animShake = AnimationUtils.loadAnimation(SendEmailActivity.this, R.anim.shake);
        animShake.setDuration(1);
        view=findViewById(R.id.view);
        send=findViewById(R.id.send);
        email=findViewById(R.id.email);
        Pattern pat = Pattern.compile(emailRegex);
        setUpToolbar();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString().trim())){
                    view.startAnimation(animShake);
                    return;
                }
                else if(!pat.matcher(email.getText().toString()).matches())
                {
                    email.setError("Enter a valid email");
                }
                else
                {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SendEmailActivity.this, "Email sent successsfully", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(),LogIn.class);
                                        startActivity(i);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void setUpToolbar()
    {
        toolbar = findViewById(R.id.toolbar_send_email);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),LogIn.class);
        startActivity(i);
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent i=new Intent(getApplicationContext(),LogIn.class);
        startActivity(i);
        return true;
    }
}