package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensearo.Adapters.IconAdapter;
import com.example.expensearo.JavaClasses.IconModel;
import com.example.expensearo.JavaClasses.Utility;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddIncome extends AppCompatActivity {
EditText category_name;
RecyclerView icons_grid;
IconAdapter iconAdapter;
DatabaseReference iconReference;
    Toolbar toolbar;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        category_name = findViewById(R.id.category_name);
        icons_grid = findViewById(R.id.icons_grid);
        mAuth = FirebaseAuth.getInstance();
        icons_grid.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext(),140)));
        iconReference = FirebaseDatabase.getInstance().getReference().child("Icons");
        FirebaseRecyclerOptions<IconModel> options =
                new FirebaseRecyclerOptions.Builder<IconModel>()
                        .setQuery(iconReference, IconModel.class)
                        .build();
        iconAdapter = new IconAdapter(options,this);
        icons_grid.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
        iconAdapter.setOnItemClickListener(new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final String name = category_name.getText().toString();
                if (name.equals(""))
                    category_name.setError("Please enter a name");
                else {
                    iconReference.child(iconAdapter.getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                String icon = snapshot.child("image").getValue().toString();
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("Name", name);
                                map.put("Icon", icon);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Categories").child("Income").push().setValue(map);
                                Toast toast = Toast.makeText(AddIncome.this, "New income added: "+name, Toast.LENGTH_LONG);
//                                View toastView = toast.getView();
//                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
//                                toastMessage.setTextSize(15);
//                                toastMessage.setTextColor(Color.WHITE);
//                                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_done_24, 0, 0, 0);
//                                toastMessage.setGravity(Gravity.CENTER);
//                                toastMessage.setCompoundDrawablePadding(10);
//                                toastView.setBackgroundColor(Color.DKGRAY);
                                toast.show();
                                Intent i=new Intent(getApplicationContext(),IncomeActivity.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        setupToolbar();
    }
    @Override
    protected void onStart() {
        super.onStart();
        iconAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        iconAdapter.stopListening();
    }
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_income);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Income");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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