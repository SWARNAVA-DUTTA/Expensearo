package com.example.expensearo;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.Adapters.IncomeAdapter;
import com.example.expensearo.JavaClasses.Income;
import com.example.expensearo.JavaClasses.Utility;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NewIncome extends AppCompatActivity {
    TextView dateTxt,name,timeText;
    EditText amount, note;
    LinearLayout date_layout,time_layout;
    ImageView icon;
    RelativeLayout image_layout;
    Toolbar toolbar;
    RelativeLayout amount_layout;
    Button addButton;
    ImageView add_income;
    RecyclerView income_sources;
    IncomeAdapter incomeAdapter;
    DatabaseReference incomeSources;
    String imageUrl="";
    FirebaseAuth mAuth;
    RelativeLayout blank_page;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<String> arrayListTitle = new ArrayList<>();
    ArrayList<String> getArrayListLogo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);
        income_sources = findViewById(R.id.income_sources);
        icon=findViewById(R.id.income_icon);
        blank_page=findViewById(R.id.blank_page);
        add_income=findViewById(R.id.add_income);
        name=findViewById(R.id.income_name);
        timeText=findViewById(R.id.timeText);
        time_layout=findViewById(R.id.time_layout);
        amount_layout=findViewById(R.id.amount_layout);
        amount = findViewById(R.id.amount);
        image_layout=findViewById(R.id.image_layout);
        note = findViewById(R.id.note);
        addButton=findViewById(R.id.addButton);
        date_layout = findViewById(R.id.date_layout);
        dateTxt = findViewById(R.id.dateTxt);
        Intent intent = getIntent();
        final String month_name=intent.getStringExtra("monthName");
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        final DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        timeText.setText(dateFormat.format(new Date()));
        dateTxt.setText(date);
        add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AddIncomeSecond.class);
                startActivity(i);
            }
        });
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
//        amount.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                chooseCategory.setVisibility(View.VISIBLE);
//                income_sources.setVisibility(View.GONE);
//                return false;
//            }
//        });
        mAuth = FirebaseAuth.getInstance();
        income_sources.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext(), 140)));
        incomeSources = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Categories").child("Income");
        FirebaseRecyclerOptions<Income> options =
                new FirebaseRecyclerOptions.Builder<Income>()
                        .setQuery(incomeSources, Income.class)
                        .build();
        incomeAdapter = new IncomeAdapter(options, this);
        income_sources.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();
        incomeSources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0)
                    blank_page.setVisibility(View.GONE);
                else
                    income_sources.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        incomeSources.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Income Mod = dataSnapshot1.getValue(Income.class);
                    arrayListTitle.add(Mod.getName());
                    getArrayListLogo.add(Mod.getIcon());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().equals("")) {
                    amount_layout.startAnimation(blinkAnimation());
                } else if (imageUrl.equals("")) {
                    Toast.makeText(NewIncome.this, "Please choose income category", Toast.LENGTH_SHORT).show();
                }
                else
                {
                final HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("type", name.getText().toString());
                map.put("date", dateTxt.getText().toString());
                map.put("amount", amount.getText().toString());
                map.put("category", "Income");
                if (!note.getText().toString().equals("")) {
                    map.put("note", "(" + note.getText().toString() + ")");
                } else
                    map.put("note", "");
                map.put("icon", imageUrl);
                Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                map.put("time", dateFormat.format(new Date()));
                String m = "";
                if (month_name.equals("January"))
                    m = month_name;
                if (month_name.equals("February"))
                    m = month_name;
                if (month_name.equals("March"))
                    m = month_name;
                if (month_name.equals("April"))
                    m = month_name;
                if (month_name.equals("May"))
                    m = month_name;
                if (month_name.equals("June"))
                    m = month_name;
                if (month_name.equals("July"))
                    m = month_name;
                if (month_name.equals("August"))
                    m = month_name;
                if (month_name.equals("September"))
                    m = month_name;
                if (month_name.equals("October"))
                    m = month_name;
                if (month_name.equals("November"))
                    m = month_name;
                if (month_name.equals("December"))
                    m = month_name;
                final String pushId = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(m).child("All Transactions").push().getKey();

                final String finalM = m;
                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(m).child("All Transactions").child(pushId).setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(finalM).child("Income").child(pushId).setValue(map);
                                    Toast.makeText(NewIncome.this, name.getText().toString() + ": ₹" + amount.getText().toString() + " is added", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);

                                }
                            }
                        });
            }
            }
        });

        incomeAdapter.setOnItemClickListener(new IncomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                image_layout.setVisibility(View.VISIBLE);
                name.setText(arrayListTitle.get(position));
                Picasso.get().load(getArrayListLogo.get(position)).into(icon);
                imageUrl=getArrayListLogo.get(position);
            }
        });
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_new_income);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Income");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        incomeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        incomeAdapter.stopListening();
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

    private Animation blinkAnimation() {
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(50);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(7);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
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