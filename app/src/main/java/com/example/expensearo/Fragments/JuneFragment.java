package com.example.expensearo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensearo.Adapters.ExpenseAdapter;
import com.example.expensearo.Adapters.MonthlyBudgetAdapter;
import com.example.expensearo.Adapters.TabsAccessorAdapter;
import com.example.expensearo.EditTransaction;
import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.JavaClasses.Income;
import com.example.expensearo.JavaClasses.MonthlyBudget;
import com.example.expensearo.JavaClasses.Utility;
import com.example.expensearo.NewExpense;
import com.example.expensearo.NewIncome;
import com.example.expensearo.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class JuneFragment extends Fragment {
    private View juneExpenseView;
    OnSwipeTouchListener onSwipeTouchListener;
    RecyclerView expense_list,monthly_budget;
    ExpenseAdapter expenseAdapter;
    MonthlyBudgetAdapter monthlyBudgetAdapter;
    DatabaseReference expenseSources,monthlyBudgetRef;
    FirebaseAuth mAuth;
    FoldingCell fc;
    TextView month_name;
    FrameLayout cell_content_view;
    Calendar c = Calendar.getInstance();
    ArrayList<String> arrayListTitle = new ArrayList<>();
    ArrayList<String> getArrayListLogo = new ArrayList<>();
    TextView totalIncome,totalExpense,balance_left;
    FloatingActionButton income;
    public JuneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        juneExpenseView=inflater.inflate(R.layout.fragment_june, container, false);
        mAuth=FirebaseAuth.getInstance();
        income=juneExpenseView.findViewById(R.id.income);
        cell_content_view=juneExpenseView.findViewById(R.id.cell_content_view);
        onSwipeTouchListener = new JuneFragment.OnSwipeTouchListener(getActivity().getApplicationContext(), juneExpenseView.findViewById(R.id.view));
        String currentUserId=mAuth.getCurrentUser().getUid();
        totalIncome=juneExpenseView.findViewById(R.id.totalIncome);
        totalExpense=juneExpenseView.findViewById(R.id.totalExpense);
        balance_left=juneExpenseView.findViewById(R.id.balance_left);
        expense_list=juneExpenseView.findViewById(R.id.expense_list);
        monthly_budget=juneExpenseView.findViewById(R.id.monthly_budget);
        fc = (FoldingCell) juneExpenseView.findViewById(R.id.folding_cell);
        month_name=juneExpenseView.findViewById(R.id.month_name);
        int month = c.get(Calendar.MONTH);
        String m = getMonth(month+1);
        int year = c.get(Calendar.YEAR);
        expense_list.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), Utility.calculateNoOfColumns(getActivity().getApplicationContext(),140)));
       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        monthly_budget.setLayoutManager(linearLayoutManager);
        expenseSources= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child("Expense");
        monthlyBudgetRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child(String.valueOf(year)).child("June");
        FirebaseRecyclerOptions<MonthlyBudget> budget =
                new FirebaseRecyclerOptions.Builder<MonthlyBudget>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child(String.valueOf(year)).child("June").child("All Transactions").orderByChild("date"), MonthlyBudget.class)
                        .build();
        monthlyBudgetAdapter=new MonthlyBudgetAdapter(budget,getActivity().getApplicationContext());
        monthly_budget.setAdapter(monthlyBudgetAdapter);
        monthlyBudgetAdapter.notifyDataSetChanged();
        FirebaseRecyclerOptions<Expense> options =
                new FirebaseRecyclerOptions.Builder<Expense>()
                        .setQuery(expenseSources, Expense.class)
                        .build();

        expenseAdapter= new ExpenseAdapter(options,getActivity().getApplicationContext());
        expense_list.setAdapter(expenseAdapter);
        expenseAdapter.notifyDataSetChanged();

        expenseSources.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Expense Mod = dataSnapshot1.getValue(Expense.class);
                    arrayListTitle.add(Mod.getName());
                    getArrayListLogo.add(Mod.getIcon());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i=new Intent(getActivity().getApplicationContext(), NewExpense.class);
                i.putExtra("expense_name",arrayListTitle.get(position));
                i.putExtra("expense_icon",getArrayListLogo.get(position));
                i.putExtra("monthName","June");
                startActivity(i);
            }
        });
        final int[] total_income_array = new int[99];
        monthlyBudgetAdapter.setOnItemClickListener(new MonthlyBudgetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final String keyId=monthlyBudgetAdapter.getRef(position).getKey();
                monthlyBudgetRef.child("All Transactions").child(monthlyBudgetAdapter.getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category=snapshot.child("category").getValue().toString();
                        Intent i=new Intent(getActivity().getApplicationContext(), EditTransaction.class);
                        i.putExtra("category",category);
                        i.putExtra("monthName","June");
                        i.putExtra("keyId", keyId);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        month_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent income=new Intent(getActivity().getApplicationContext(),NewIncome.class);
                income.putExtra("monthName","June");
                startActivity(income);
            }
        });
        calculateTotalIncome();
        calculateTotalExpense();
        calculateBalanceLeft();
        return juneExpenseView;
    }



    @Override
    public void onStart() {
        super.onStart();
        expenseAdapter.startListening();
        monthlyBudgetAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        expenseAdapter.stopListening();
        monthlyBudgetAdapter.stopListening();
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

    private void calculateBalanceLeft()
    {
        monthlyBudgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("Total Income") && snapshot.hasChild("Total Expense"))
                {
                    String totalincome=snapshot.child("Total Income").getValue().toString();
                    String totalexpense=snapshot.child("Total Expense").getValue().toString();
                    int balance=Integer.parseInt(totalincome)-Integer.parseInt(totalexpense);
                    
                    balance_left.setText(String.valueOf(balance));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calculateTotalExpense()
    {
        final int[] total_expense_array = new int[99];
        monthlyBudgetRef.child("Expense").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String total_expense = (String) dataSnapshot1.child("amount").getValue();
                    total_expense_array[i]=Integer.parseInt(total_expense);
                    i++;
                }
                int total=0;
                for(i=0;i<total_expense_array.length;i++)
                {
                    total=total+total_expense_array[i];
                }

                monthlyBudgetRef.child("Total Expense").setValue(String.valueOf(total));
                monthlyBudgetRef.child("Total Expense")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    String total=snapshot.getValue().toString();
                                    totalExpense.setText(total);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void calculateTotalIncome()
    {
        final int[] total_income_array = new int[99];
        monthlyBudgetRef.child("Income").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String income = (String) dataSnapshot1.child("amount").getValue();
                    total_income_array[i]=Integer.parseInt(income);
                    i++;
                }
                int total=0;
                for(i=0;i<total_income_array.length;i++)
                {
                    total=total+total_income_array[i];
                }

                monthlyBudgetRef.child("Total Income").setValue(String.valueOf(total));
                monthlyBudgetRef.child("Total Income")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    String total=snapshot.getValue().toString();
                                    totalIncome.setText(total);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    public class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;
        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new JuneFragment.OnSwipeTouchListener.GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffX > 0) {
//                                onSwipeRight();
//                            } else {
//                                onSwipeLeft();
//                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
//        void onSwipeRight() {
//            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
//            this.onSwipe.swipeRight();
//        }
//        void onSwipeLeft() {
//            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
//            this.onSwipe.swipeLeft();
    }
    void onSwipeTop() {
        fc.fold(false);
        this.onSwipe.swipeTop();
    }
    void onSwipeBottom() {
        fc.unfold(false);
        this.onSwipe.swipeBottom();
    }
    interface onSwipeListener {
        void swipeRight();
        void swipeTop();
        void swipeBottom();
        void swipeLeft();
    }
    JuneFragment.onSwipeListener onSwipe;
}