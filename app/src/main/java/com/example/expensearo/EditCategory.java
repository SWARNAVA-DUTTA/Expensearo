package com.example.expensearo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.expensearo.Adapters.IconAdapter;
import com.example.expensearo.Adapters.IncomeAdapter;
import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.JavaClasses.IconModel;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditCategory extends AppCompatActivity {
    EditText category_name;
    RecyclerView icons_grid;
    IconAdapter iconAdapter;
    DatabaseReference iconReference,categoryReference;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    ImageView icon;
    Button changeButton;
    String imageUrl="";
    ArrayList<String> arrayListTitleIncome = new ArrayList<>();
    ArrayList<String> arrayListIcon = new ArrayList<>();
    ArrayList<String> arrayListTitleExpense = new ArrayList<>();
    ArrayList<String> arrayListIconExpense = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        category_name = findViewById(R.id.category_name);
        icons_grid = findViewById(R.id.icons_grid);
        changeButton=findViewById(R.id.changeButton);
        icon=findViewById(R.id.icon);
        mAuth = FirebaseAuth.getInstance();
        String currentUserId=mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        final String category=intent.getStringExtra("category");
        final String key=intent.getStringExtra("key");
        icons_grid.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext(),140)));
        iconReference = FirebaseDatabase.getInstance().getReference().child("Icons");
        categoryReference=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child(category);
        FirebaseRecyclerOptions<IconModel> options =
                new FirebaseRecyclerOptions.Builder<IconModel>()
                        .setQuery(iconReference, IconModel.class)
                        .build();
        iconAdapter = new IconAdapter(options,this);
        icons_grid.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
        iconReference.addValueEventListener(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                   for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                                       IconModel Mod = dataSnapshot1.getValue(IconModel.class);
                                                                       arrayListIcon.add(Mod.getImage());
                                                                   }
                                                               }

                                                               @Override
                                                               public void onCancelled(@NonNull DatabaseError error) {

                                                               }
                                                           });
                categoryReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String retrieveName = snapshot.child("Name").getValue().toString();
                        String retrieveIcon = snapshot.child("Icon").getValue().toString();
                        category_name.setText(retrieveName);
                        Picasso.get().load(retrieveIcon).into(icon);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        iconAdapter.setOnItemClickListener(new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Picasso.get().load(arrayListIcon.get(position)).into(icon);
                imageUrl=arrayListIcon.get(position);
            }
        });
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("Name", category_name.getText().toString());
                if(!imageUrl.equals(""))
                {
                    map.put("Icon",imageUrl);
                }
                categoryReference.child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditCategory.this, category+" Updated", Toast.LENGTH_SHORT).show();
                        if(category.equals("Income")) {
                            Intent i = new Intent(getApplicationContext(), IncomeActivity.class);
                            startActivity(i);
                        }
                        else if(category.equals("Expense"))
                        {
                            Intent i = new Intent(getApplicationContext(), ExpenseActivity.class);
                            startActivity(i);
                        }
                    }
                });

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
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_category);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        final String category=intent.getStringExtra("category");
        if(category.equals("Income"))
            getSupportActionBar().setTitle("Edit Income");
        if(category.equals("Expense"))
            getSupportActionBar().setTitle("Edit Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                final Intent intent = getIntent();
                final String category=intent.getStringExtra("category");
                final String key=intent.getStringExtra("key");
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want delete this "+category+"?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                                .child("Categories").child(category).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditCategory.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                                if(category.equals("Income")) {
                                    Intent i = new Intent(getApplicationContext(), IncomeActivity.class);
                                    startActivity(i);
                                }
                                    else if(category.equals("Expense")){
                                    Intent i = new Intent(getApplicationContext(), ExpenseActivity.class);
                                    startActivity(i);
                                }
                                finish();
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
}