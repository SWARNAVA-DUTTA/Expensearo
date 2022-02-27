package com.example.expensearo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.Adapters.IncomeAdapter;
import com.example.expensearo.JavaClasses.Income;
import com.example.expensearo.JavaClasses.Utility;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IncomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView income_sources;
    IncomeAdapter incomeAdapter;
    DatabaseReference incomeSources;
    FirebaseAuth mAuth;
    LinearLayout blank_page;
    ArrayList<String> arrayListTitle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        setupToolbar();
        income_sources=findViewById(R.id.income_sources);
        mAuth=FirebaseAuth.getInstance();
        blank_page=findViewById(R.id.blank_page);
        String currentUserId=mAuth.getCurrentUser().getUid();
        income_sources.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext(),140)));
        incomeSources = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child("Income");
        FirebaseRecyclerOptions<Income> options =
                new FirebaseRecyclerOptions.Builder<Income>()
                        .setQuery(incomeSources, Income.class)
                        .build();
        incomeAdapter= new IncomeAdapter(options,this);
        income_sources.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();
        incomeSources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0)
                    blank_page.setVisibility(View.GONE);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        incomeAdapter.setOnItemClickListener(new IncomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
//                Toast.makeText(IncomeActivity.this, incomeAdapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(), EditCategory.class);
                i.putExtra("category","Income");
                i.putExtra("key",incomeAdapter.getRef(position).getKey());
                startActivity(i);
            }
        });
    }
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_income);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Income");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add_item:
                Intent i=new Intent(getApplicationContext(),AddIncome.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        return true;
    }
}