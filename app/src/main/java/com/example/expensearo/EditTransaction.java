package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import com.example.expensearo.Adapters.ExpenseAdapter;
import com.example.expensearo.Adapters.IncomeAdapter;
import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.JavaClasses.Income;
import com.example.expensearo.JavaClasses.Utility;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditTransaction extends AppCompatActivity {
    TextView cancel_btn,ok_btn,dateTxt,name,timeText;
    EditText amount,note;
    LinearLayout date_layout,time_layout;
    CalendarView calender;
    Toolbar toolbar;
    Button changeButton;
    FirebaseAuth mAuth;
    RelativeLayout image_layout;
    ImageView icon;
    RelativeLayout amount_layout;
    RecyclerView sources;
    DatabaseReference expenseSources,incomeSources;
    ExpenseAdapter expenseAdapter;
    IncomeAdapter incomeAdapter;
    String imageUrl="";
    AlertDialog.Builder builder;
    ArrayList<String> arrayListTitleExpense = new ArrayList<>();
    ArrayList<String> getArrayListLogoExpense = new ArrayList<>();
    ArrayList<String> arrayListTitleIncome = new ArrayList<>();
    ArrayList<String> getArrayListLogoIncome = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        mAuth=FirebaseAuth.getInstance();
        sources=findViewById(R.id.sources);
        builder = new AlertDialog.Builder(this);

        amount=findViewById(R.id.amount);
        icon=findViewById(R.id.transaction_icon);
        image_layout=findViewById(R.id.image_layout);
        name=findViewById(R.id.transaction_name);
        note=findViewById(R.id.note);
        amount_layout=findViewById(R.id.amount_layout);
        date_layout=findViewById(R.id.date_layout);
        timeText=findViewById(R.id.timeText);
        time_layout=findViewById(R.id.time_layout);
        dateTxt=findViewById(R.id.dateTxt);
        final String currentUserId=mAuth.getCurrentUser().getUid();
        expenseSources= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child("Expense");
        incomeSources= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child("Income");
        sources.setLayoutManager(new GridLayoutManager(getApplicationContext(), Utility.calculateNoOfColumns(getApplicationContext(),140)));
        FirebaseRecyclerOptions<Income> optionsIncome =
                new FirebaseRecyclerOptions.Builder<Income>()
                        .setQuery(incomeSources, Income.class)
                        .build();

        FirebaseRecyclerOptions<Expense> optionsExpense =
                new FirebaseRecyclerOptions.Builder<Expense>()
                        .setQuery(expenseSources, Expense.class)
                        .build();

        incomeAdapter= new IncomeAdapter(optionsIncome,this);
        expenseAdapter= new ExpenseAdapter(optionsExpense,this);
        Intent intent = getIntent();
        final String category=intent.getStringExtra("category");
        final String month_name=intent.getStringExtra("monthName");
        final String keyId=intent.getStringExtra("keyId");
        incomeSources.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Expense Mod = dataSnapshot1.getValue(Expense.class);
                    arrayListTitleIncome.add(Mod.getName());
                    getArrayListLogoIncome.add(Mod.getIcon());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        expenseSources.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Expense Mod = dataSnapshot1.getValue(Expense.class);
                    arrayListTitleExpense.add(Mod.getName());
                    getArrayListLogoExpense.add(Mod.getIcon());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(category.equals("Income"))
        {
            sources.setAdapter(incomeAdapter);
            incomeAdapter.notifyDataSetChanged();
            incomeAdapter.setOnItemClickListener(new IncomeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    sources.setVisibility(View.GONE);
                    changeButton.setVisibility(View.VISIBLE);
                    name.setText(arrayListTitleIncome.get(position));
                    Picasso.get().load(getArrayListLogoIncome.get(position)).into(icon);
                    imageUrl=getArrayListLogoIncome.get(position);
                }
            });

        }
        if(category.equals("Expense"))
        {
            sources.setAdapter(expenseAdapter);
            expenseAdapter.notifyDataSetChanged();
            expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    sources.setVisibility(View.GONE);
                    changeButton.setVisibility(View.VISIBLE);
                    name.setText(arrayListTitleExpense.get(position));
                    Picasso.get().load(getArrayListLogoExpense.get(position)).into(icon);
                    imageUrl=getArrayListLogoExpense.get(position);
                }
            });
        }
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
        changeButton=findViewById(R.id.changeButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, Object> map = new HashMap<String, Object>();
                if (amount.getText().toString().equals(""))
                {
                    amount_layout.startAnimation(blinkAnimation());
                }
                else
                {
                map.put("type", name.getText().toString());
                map.put("time", timeText.getText().toString());
                map.put("date", dateTxt.getText().toString());
                if (!note.getText().toString().equals("")) {
                    map.put("note", "(" + note.getText().toString() + ")");
                }
                if (!imageUrl.equals(""))
                    map.put("icon", imageUrl);
                Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child(String.valueOf(year)).child(month_name)
                        .child("All Transactions").child(keyId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child(String.valueOf(year)).child(month_name)
                                .child(category).child(keyId).updateChildren(map);
                        Toast.makeText(EditTransaction.this, "Your data is updated successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
            }

        });
        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButton.setVisibility(View.GONE);
                sources.setVisibility(View.VISIBLE);
            }
        });

        retrieveTransactionInfo(month_name,category,keyId);
        setupToolbar(category);
    }



    private void retrieveTransactionInfo(String month_name, String category, String keyId)
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year))
                    .child(month_name).child("All Transactions").child(keyId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String retrieveAmount=snapshot.child("amount").getValue().toString();
                    String retrieveDate=snapshot.child("date").getValue().toString();
                    String retrieveIcon=snapshot.child("icon").getValue().toString();
                    String retrieveTime=snapshot.child("time").getValue().toString();
                    String retrieveType=snapshot.child("type").getValue().toString();
                    String retrieveNote=snapshot.child("note").getValue().toString();
                    if(!retrieveNote.equals(""))
                    {
                        StringBuilder sb = new StringBuilder(retrieveNote);
                        sb.deleteCharAt(retrieveNote.length() - 1);
                        sb.deleteCharAt(0);
                        note.setText(sb.toString());
                    }

                    amount.setText(retrieveAmount);
                    dateTxt.setText(retrieveDate);
                    Picasso.get().load(retrieveIcon).into(icon);
                    timeText.setText(retrieveTime);
                    name.setText(retrieveType);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    private void setupToolbar(String category) {
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_transaction);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit "+category) ;
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


    @Override
    public void onStart() {
        super.onStart();
        expenseAdapter.startListening();
        incomeAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        expenseAdapter.stopListening();
        incomeAdapter.stopListening();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_transaction_or_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.delete_item:
                final AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want delete this item?");
                Intent intent = getIntent();
                final String keyId=intent.getStringExtra("keyId");
                Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = getIntent();
                        final String month_name=intent.getStringExtra("monthName");
                        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                                .child(String.valueOf(year)).child(month_name).child("All Transactions").child(keyId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String category=snapshot.child("category").getValue().toString();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(month_name)
                                        .child(category).child(keyId).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(year)).child(month_name)
                                        .child("All Transactions").child(keyId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditTransaction.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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