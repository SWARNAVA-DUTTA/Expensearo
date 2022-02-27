package com.example.expensearo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

//import com.github.dhaval2404.imagepicker.ImagePicker;

public class Profile extends AppCompatActivity{
    LinearLayout email_layout;
CircleImageView profile_pic;
TextView user_name,user_email;
FirebaseAuth mAuth;
boolean checkPermission=false;
Button proceed_btn;
ProgressDialog loading;
ImageView edit_image;
int flag=0;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email_layout=findViewById(R.id.email_layout);
        profile_pic=findViewById(R.id.profile_pic);
        user_name=findViewById(R.id.user_name);
        user_email=findViewById(R.id.user_email);
        proceed_btn=findViewById(R.id.proceed_btn);
        loading=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        proceed_btn.setVisibility(View.VISIBLE);
        edit_image=findViewById(R.id.edit_image);
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        edit_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changename();
            }
        });
        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Email cannot be changed", Toast.LENGTH_SHORT).show();
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Dexter.withActivity(Profile.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {checkPermission=true;pick_profile_picture();}
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {checkPermission=false;}
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {token.continuePermissionRequest();}
                        }).check();

            }
        });
        retrieveUserinfo();
        setupToolbar();

    }
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    private void changename()
    {
        String uid = mAuth.getCurrentUser().getUid();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View changing_name = inflater.inflate(R.layout.change_name_dialog, null);
        final TextView msg=changing_name.findViewById(R.id.msg);
        final EditText ed_name = changing_name.findViewById(R.id.ed_name);

        dialog.setView(changing_name);
        ed_name.setSelectAllOnFocus(true);
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((snapshot.exists()) && (snapshot.hasChild("name")) && (snapshot.hasChild("email"))) {
                            String retrieveUserNamefromdb = snapshot.child("name").getValue().toString();
                            ed_name.setText(retrieveUserNamefromdb);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String getuserName = ed_name.getText().toString();
                if (TextUtils.isEmpty(getuserName)) {
                    Toast.makeText(Profile.this, "Name can't remain empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("name").setValue(getuserName);
                }
            }

        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        dialog.show();
    }

    private void pick_profile_picture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
//        ImagePicker.Companion.with(Profile.this)
//                .crop()	    			//Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode==123) {

            if (data != null) {
                Uri fileUri = data.getData();
//                profile_picture.setImageURI(fileUri);
                loading.setMessage("Uploading profile picture...");
                loading.setCanceledOnTouchOutside(false);
                loading.show();
                final String currentUserId = mAuth.getCurrentUser().getUid();
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Profile_Pictures")
                        .child(currentUserId+".jpg");
//                StorageReference filePath=userProfileImage.child(currentUserId+".jpg");
                storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri urlImage = uriTask.getResult();
                        String ImageUrl = urlImage.toString();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("profile_picture").setValue(ImageUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        flag=flag+1;
                                        Toast.makeText(Profile.this, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                                        loading.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Profile.this, "Failed to upload Profile Picture", Toast.LENGTH_SHORT).show();
                                        loading.dismiss();
                                    }
                                });

                    }
                });
            }
//            else if (resultCode == ImagePicker.RESULT_ERROR) {
//                Toast.makeText(this, "ImagePicker.getError(data)", Toast.LENGTH_SHORT).show();
//            }
        else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void retrieveUserinfo() {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((snapshot.exists()) && (snapshot.hasChild("name")) && (snapshot.hasChild("profile_picture")) && (snapshot.hasChild("email"))) {
                            String retrieveUserName = snapshot.child("name").getValue().toString();
                            String retrieveUserEmail = snapshot.child("email").getValue().toString();
                            String retrieveUserImage = snapshot.child("profile_picture").getValue().toString();
                            user_name.setText(retrieveUserName);
                            user_email.setText(retrieveUserEmail);
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
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}