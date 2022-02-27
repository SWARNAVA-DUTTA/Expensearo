package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.JavaClasses.Income;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NewExpense extends AppCompatActivity {
    TextView dateTxt,name,timeText;
    EditText amount,note;
    LinearLayout date_layout,time_layout;
    Toolbar toolbar;
    Button addButton;
    FirebaseAuth mAuth;
    RelativeLayout image_layout;
    ImageView icon;
    RelativeLayout amount_layout;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        mAuth=FirebaseAuth.getInstance();
        amount=findViewById(R.id.amount);
        icon=findViewById(R.id.expense_icon);
        image_layout=findViewById(R.id.image_layout);
        name=findViewById(R.id.expense_name);
        note=findViewById(R.id.note);
        amount_layout=findViewById(R.id.amount_layout);
        date_layout=findViewById(R.id.date_layout);
        dateTxt=findViewById(R.id.dateTxt);
        timeText=findViewById(R.id.timeText);
        time_layout=findViewById(R.id.time_layout);
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        final DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        timeText.setText(dateFormat.format(new Date()));
        dateTxt.setText(date);
        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calenderDialog();
            }
        });
        time_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog();
            }
        });
        amount.requestFocus();
        addButton=findViewById(R.id.addButton);
        Intent intent = getIntent();
        final String expense_name=intent.getStringExtra("expense_name");
        final String expense_icon=intent.getStringExtra("expense_icon");
        final String month_name=intent.getStringExtra("monthName");
//        addButton.setText("Add "+expense_name);
        Picasso.get().load(expense_icon).into(icon);
        name.setText(expense_name);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (amount.getText().toString().equals("")) {
                    amount_layout.startAnimation(blinkAnimation());
                }
                else {
                    final HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("type", expense_name);
                    map.put("date", dateTxt.getText().toString());
                    map.put("amount", amount.getText().toString());
                    map.put("category","Expense");
                    if (!note.getText().toString().equals("")) {
                        map.put("note", "(" + note.getText().toString() + ")");
                    }
                    else
                        map.put("note","");
                    map.put("icon", expense_icon);
                    map.put("time",dateFormat.format(new Date()));
                    Calendar c = Calendar.getInstance();
                    final int year = c.get(Calendar.YEAR);
                    String m="";
                    if (month_name.equals("January")){
                       m=month_name;}
                    if (month_name.equals("February"))
                        m=month_name;
                    if (month_name.equals("March"))
                        m=month_name;
                    if (month_name.equals("April"))
                        m=month_name;
                    if (month_name.equals("May"))
                        m=month_name;
                    if (month_name.equals("June"))
                        m=month_name;
                    if (month_name.equals("July"))
                        m=month_name;
                    if (month_name.equals("August"))
                        m=month_name;
                    if (month_name.equals("September"))
                        m=month_name;
                    if (month_name.equals("October"))
                        m=month_name;
                    if (month_name.equals("November"))
                        m=month_name;
                    if (month_name.equals("December"))
                        m=month_name;

                    final String finalM = m;
                    final String pushId=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(m).child("All Transactions").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(m).child("All Transactions").child(pushId).setValue(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(finalM).child("Expense").child(pushId).setValue(map);
                                            Toast.makeText(NewExpense.this, expense_name + ": ₹" + amount.getText().toString() + " is added", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(i);

                                        }
                                    }
                                });

            }
            }
        });
        amount.setOnTouchListener(new View.OnTouchListener() {
                                      @Override
                                      public boolean onTouch(View v, MotionEvent event) {
                                          return false;
                                      }
                                  });
                setupToolbar();
    }
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_new_expense);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void calenderDialog()
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if(String.valueOf(dayOfMonth).length()==1)
                        {
                            String monthinString=getMonth(monthOfYear+1);
                            String date= "0"+dayOfMonth+"-"+monthinString+"-"+year;
                            dateTxt.setText(date);
                        }
                        else {
                            String monthinString=getMonth(monthOfYear+1);
                            String date = dayOfMonth + "-" + monthinString + "-" + year;
                            dateTxt.setText(date);
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void timeDialog()
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        if(String.valueOf(hourOfDay).length()>1)
                            timeText.setText(hourOfDay + ":" + minute+" "+AM_PM);
                        else
                            timeText.setText("0"+hourOfDay + ":" + minute+" "+AM_PM);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    private String getMonth(int month) {
        String m="";
        if(month==1)
        {
            m="Jan";
        }
        else if(month==2)
        {
            m="Feb";
        }
        else if(month==3)
        {
            m="Mar";
        }
        else if(month==4)
        {
            m="Apr";
        }
        else if(month==5)
        {
            m="May";
        }
        else if(month==6)
        {
            m="Jun";
        }
        else if(month==7)
        {
            m="Jul";
        }
        else if(month==8)
        {
            m="Aug";
        }
        else if(month==9)
        {
            m="Sep";
        }
        else if(month==10)
        {
            m="Oct";
        }
        else if(month==11)
        {
            m="Nov";
        }
        else if(month==12)
        {
            m="Dec";
        }
        return m;
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
    private Animation blinkAnimation() {
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(50);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(7);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
    }

}