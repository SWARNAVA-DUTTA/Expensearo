package com.example.expensearo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class Settings extends AppCompatActivity {
    CircleImageView profile_pic;
    TextView user_name,delete_txt,cancel_txt,alertTitle,alertMessage,submit_btn,cancel_btn,text,text2;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    GifImageView gif;
    ImageView check;
    LinearLayout btn_cancel,btn_delete;
    RelativeLayout profile_section,log_out_layout,rate_layout,btn_layout;
//    SmileRating smileRating;
    int rating=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profile_pic=findViewById(R.id.profile_picture);
        user_name=findViewById(R.id.user_name);
        mAuth = FirebaseAuth.getInstance();
        profile_section=findViewById(R.id.profile_section);
        log_out_layout=findViewById(R.id.log_out_layout);
        rate_layout=findViewById(R.id.rate_layout);
        log_out_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                confirm_logout();
            }
        });
//        rate_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ratingDialog();
//            }
//        });
        profile_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Profile.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        retrieveUserinfo();
        setupToolbar();
    }

//    private void ratingDialog()
//    {
//
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View promptView = layoutInflater.inflate(R.layout.custom_rating_dialog, null);
//
//        final AlertDialog alertD = new AlertDialog.Builder(this).create();
//        check=promptView.findViewById(R.id.check);
//        text2=promptView.findViewById(R.id.text2);
//        btn_layout=promptView.findViewById(R.id.btn_layout);
//        text=promptView.findViewById(R.id.text);
//        smileRating=promptView.findViewById(R.id.smile_rating);
//
//        submit_btn=promptView.findViewById(R.id.submit_btn);
//        cancel_btn=promptView.findViewById(R.id.cancel_btn);
//        FirebaseDatabase.getInstance().getReference().child("Rating").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    String rating=snapshot.child(mAuth.getCurrentUser().getUid()).getValue().toString();
//                    if(rating.equals("1"))
//                        smileRating.setSelectedSmile(BaseRating.TERRIBLE);
//                    if(rating.equals("2"))
//                        smileRating.setSelectedSmile(BaseRating.BAD);
//                    if(rating.equals("3"))
//                        smileRating.setSelectedSmile(BaseRating.OKAY);
//                    if(rating.equals("4"))
//                        smileRating.setSelectedSmile(BaseRating.GOOD);
//                    if(rating.equals("5"))
//                        smileRating.setSelectedSmile(BaseRating.GREAT);
//                }
//
//                else
//                    smileRating.setSelectedSmile(BaseRating.NONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
//            @Override
//            public void onSmileySelected(int smiley, boolean reselected) {
//                rating=smileRating.getRating();
//            }
//        });
//        cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                alertD.dismiss();
//            }
//        });
//        submit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (rating != 0){
//                    FirebaseDatabase.getInstance().getReference().child("Rating").child(mAuth.getCurrentUser().getUid()).setValue(String.valueOf(rating))
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    text.setVisibility(View.GONE);
//                                    btn_layout.setVisibility(View.GONE);
//                                    smileRating.setVisibility(View.GONE);
//                                    text2.setVisibility(View.VISIBLE);
//                                    check.setVisibility(View.VISIBLE);
//                                    new CountDownTimer(3000, 1000) {
//
//                                        @Override
//                                        public void onTick(long millisUntilFinished) {
//                                            // TODO Auto-generated method stub
//
//                                        }
//
//                                        @Override
//                                        public void onFinish() {
//                                            // TODO Auto-generated method stub
//
//                                            alertD.dismiss();
//                                        }
//                                    }.start();
//                                }
//                            });
//
//            }
//
//            }
//        });
//        alertD.getWindow().setGravity(Gravity.NO_GRAVITY);
//        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertD.setView(promptView);
//        alertD.setCanceledOnTouchOutside(true);
//        alertD.setCancelable(true);
//        alertD.show();
//    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void confirm_logout()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.log_out_confirm_dialog, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        gif=promptView.findViewById(R.id.gif);
        alertTitle=promptView.findViewById(R.id.alertTitle);
        alertMessage=promptView.findViewById(R.id.alertMessage);
        delete_txt=promptView.findViewById(R.id.delete_txt);
        cancel_txt=promptView.findViewById(R.id.cancel_txt);
        btn_cancel=promptView.findViewById(R.id.btn_cancel);
        btn_delete=promptView.findViewById(R.id.btn_delete);
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alertD.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences =getSharedPreferences("Reg", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent i=new Intent(getApplicationContext(),LogIn.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
                finish();



            }
        });
        alertD.getWindow().setGravity(Gravity.NO_GRAVITY);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertD.setView(promptView);
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(true);
        alertD.show();
    }
    private void retrieveUserinfo() {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((snapshot.exists()) && (snapshot.hasChild("name")) && (snapshot.hasChild("profile_picture"))) {
                            String retrieveUserName = snapshot.child("name").getValue().toString();
                            String retrieveUserImage = snapshot.child("profile_picture").getValue().toString();
                            user_name.setText(retrieveUserName);
                            Picasso.get().load(retrieveUserImage)
                                    .fit().centerInside()
                                    .into(profile_pic);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}