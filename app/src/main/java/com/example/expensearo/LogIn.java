package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
EditText user_email,user_password;
TextView reg_text;
LinearLayout login_btn;
    CheckBox rmbr;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    TextView error,forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        user_email=findViewById(R.id.user_email);
        user_password=findViewById(R.id.user_password);
        reg_text=findViewById(R.id.reg_text);
        rmbr=findViewById(R.id.rmbr);
        login_btn=findViewById(R.id.login_btn);
        error=findViewById(R.id.error);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        forgot_password=findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmailActivity();

            }
        });
        reg_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void openEmailActivity() {
        Intent i=new Intent(getApplicationContext(),SendEmailActivity.class);
        startActivity(i);
    }

    public void login()
    {
        if(TextUtils.isEmpty(user_email.getText().toString().trim())){
            user_email.setError("Please enter your email");
            return;
        }
        else if(TextUtils.isEmpty(user_password.getText().toString().trim())){
            user_password.setError("Please enter your password");
            return;
        }
        else {
            progressDialog.setMessage("Logging in...Please wait");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(user_email.getText().toString().trim(), user_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                error.setVisibility(View.GONE);
                                if (rmbr.isChecked()) {
                                    SharedPreferences.Editor editor = getSharedPreferences("Reg", MODE_PRIVATE).edit();
                                    editor.putString("Email", user_email.getText().toString().trim());
                                    editor.putString("Password", user_password.getText().toString().trim());
                                    editor.apply();
                                    progressDialog.dismiss();
                                    Intent i = new Intent(LogIn.this, MainActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in_right,
                                            R.anim.slide_out_left);

                                    finish();
                                }
                                else
                                {
                                    Intent i = new Intent(LogIn.this, MainActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in_right,
                                            R.anim.slide_out_left);

                                    finish();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            error.setVisibility(View.VISIBLE);
                        }
                    });

        }
    }
    @Override
    public void onBackPressed()
    {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}