package com.example.expensearo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.Adapters.ExpenseAdapter;
import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.JavaClasses.Utility;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ExpenseActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView expense_sources;
    ExpenseAdapter expenseAdapter;
    DatabaseReference expenseSources;
    FirebaseAuth mAuth;
    LinearLayout blank_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setupToolbar();
        expense_sources=findViewById(R.id.expense_sources);
        mAuth=FirebaseAuth.getInstance();
        blank_page=findViewById(R.id.blank_page);
        String currentUserId=mAuth.getCurrentUser().getUid();
        expense_sources.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext(),140)));
        expenseSources = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child("Expense");
        FirebaseRecyclerOptions<Expense> options =
                new FirebaseRecyclerOptions.Builder<Expense>()
                        .setQuery(expenseSources, Expense.class)
                        .build();
        expenseAdapter= new ExpenseAdapter(options,this);
        expense_sources.setAdapter(expenseAdapter);
        expenseAdapter.notifyDataSetChanged();
        expenseSources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0)
                    blank_page.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
//        expenseSources.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                    Expense Mod = dataSnapshot1.getValue(Expense.class);
//                    arrayListTitle.add(Mod.getName());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent i=new Intent(getApplicationContext(), EditCategory.class);
                i.putExtra("category","Expense");
                i.putExtra("key",expenseAdapter.getRef(position).getKey());
                startActivity(i);
            }
        });
    }
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_expense);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expense");
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
                Intent i=new Intent(getApplicationContext(),AddExpense.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        expenseAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        expenseAdapter.stopListening();
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