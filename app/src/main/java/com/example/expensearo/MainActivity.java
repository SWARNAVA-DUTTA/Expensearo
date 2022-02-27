package com.example.expensearo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.example.expensearo.Adapters.ExpenseAdapter;
import com.example.expensearo.Adapters.MonthlyBudgetAdapter;
import com.example.expensearo.Adapters.TabsAccessorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {
    private TabsAccessorAdapter tabsAccessorAdapter;
//    OnSwipeTouchListener onSwipeTouchListener;
    RecyclerView expense_list,monthly_budget;
    ExpenseAdapter expenseAdapter;
    MonthlyBudgetAdapter monthlyBudgetAdapter;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    DatabaseReference expenseSources,monthlyBudgetRef;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    CircleImageView profile_picture;
    TextView name;
    FloatingActionButton income;
    FoldingCell fc;
    FrameLayout cell_content_view;
    Calendar c = Calendar.getInstance();
    ArrayList<String> arrayListTitle = new ArrayList<>();
    ArrayList<String> getArrayListLogo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
//        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.view));
//        String currentUserId=mAuth.getCurrentUser().getUid();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(false);
        name=navigationView.getHeaderView(0).findViewById(R.id.name);
        profile_picture=navigationView.getHeaderView(0).findViewById(R.id.profile_picture);
//        expense_list=findViewById(R.id.expense_list);
//        monthly_budget=findViewById(R.id.monthly_budget);
//        fc = (FoldingCell) findViewById(R.id.folding_cell);
//        cell_content_view=findViewById(R.id.cell_content_view);
//        String m = getMonth(month);
//        int year = c.get(Calendar.YEAR);
//        expense_list.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext(),140)));
//        monthly_budget.setLayoutManager(new LinearLayoutManager(this));
//        expenseSources=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Categories").child("Expense");
//        monthlyBudgetRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child(m+"-"+year);
//        FirebaseRecyclerOptions<MonthlyBudget> budget =
//                new FirebaseRecyclerOptions.Builder<MonthlyBudget>()
//                        .setQuery(monthlyBudgetRef.orderByChild("date"), MonthlyBudget.class)
//                        .build();
//        monthlyBudgetAdapter=new MonthlyBudgetAdapter(budget,this);
//        monthly_budget.setAdapter(monthlyBudgetAdapter);
//        monthlyBudgetAdapter.notifyDataSetChanged();
//        FirebaseRecyclerOptions<Expense> options =
//                new FirebaseRecyclerOptions.Builder<Expense>()
//                        .setQuery(expenseSources, Expense.class)
//                        .build();
//
//        expenseAdapter= new ExpenseAdapter(options,this);
//        expense_list.setAdapter(expenseAdapter);
//        expenseAdapter.notifyDataSetChanged();
//        expenseSources.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                    Expense Mod = dataSnapshot1.getValue(Expense.class);
//                    arrayListTitle.add(Mod.getName());
//                    getArrayListLogo.add(Mod.getIcon());
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent i=new Intent(getApplicationContext(),NewExpense.class);
//                i.putExtra("expense_name",arrayListTitle.get(position));
//                i.putExtra("expense_icon",getArrayListLogo.get(position));
//                startActivity(i);
//            }
//        });
//        monthlyBudgetAdapter.setOnItemClickListener(new MonthlyBudgetAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this, monthlyBudgetAdapter.getRef(position).getKey()+"clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        monthlyBudgetAdapter.setOnItemLongClickListener(new MonthlyBudgetAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position) {
//                Toast.makeText(MainActivity.this, monthlyBudgetAdapter.getRef(position).getKey()+"clicked long", Toast.LENGTH_SHORT).show();
//            }
//        });
//        fc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fc.toggle(false);
//            }
//        });
        setupToolbar();
        retrieveUserinfo();
        menuItemClick();

    }
    private void setupToolbar() {
        ViewPager viewPager = findViewById(R.id.main_tabs_pager);
        SmartTabLayout tabs = findViewById(R.id.main_tabs);
        tabs.setViewPager(viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);//Implement Hamburg Icon
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);//It takes the response of the Hamburger icon
        toggle.syncState();//It verifies whether DrawerLayout is Open or Close state
        tabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessorAdapter);
        int month = c.get(Calendar.MONTH);
        viewPager.setCurrentItem(month);
        viewPager.setPageTransformer(true, (ViewPager.PageTransformer) new TabletTransformer());
        tabs.setViewPager(viewPager);

    }
    private void menuItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.income:
                        Intent income=new Intent(getApplicationContext(),IncomeActivity.class);
                        startActivity(income);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.expense:
                        Intent expense=new Intent(getApplicationContext(),ExpenseActivity.class);
                        startActivity(expense);
                        drawerLayout.closeDrawers();
                        break;
//                    case R.id.accounts:
//                        Intent accounts=new Intent(getApplicationContext(),ExpenseActivity.class);
//                        startActivity(accounts);
//                        drawerLayout.closeDrawers();
//                        break;
                    case R.id.settings:
                        Intent settings=new Intent(getApplicationContext(),Settings.class);
                        startActivity(settings);
                        drawerLayout.closeDrawers();
                        break;
                }
//                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
                            name.setText(retrieveUserName);
                            Picasso.get().load(retrieveUserImage)
                                    .fit().centerInside()
                                    .into(profile_picture);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
////        expenseAdapter.startListening();
////        monthlyBudgetAdapter.startListening();
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        expenseAdapter.stopListening();
//        monthlyBudgetAdapter.stopListening();
//    }
//    @Override
//    protected void onPause() {
//
//        super.onPause();
//
//        expenseAdapter.stopListening();
//        monthlyBudgetAdapter.stopListening();
//    }
//    @Override
//    protected void onResume() {
//
//        super.onResume();
//
//        expenseAdapter.startListening();
//        monthlyBudgetAdapter.startListening();
//    }
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
//    public static class OnSwipeTouchListener implements View.OnTouchListener {
//        private final GestureDetector gestureDetector;
//        Context context;
//        OnSwipeTouchListener(Context ctx, View mainView) {
//            gestureDetector = new GestureDetector(ctx, new GestureListener());
//            mainView.setOnTouchListener(this);
//            context = ctx;
//        }
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            return gestureDetector.onTouchEvent(event);
//        }
//
//        public class GestureListener extends
//                GestureDetector.SimpleOnGestureListener {
//            private static final int SWIPE_THRESHOLD = 100;
//            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                boolean result = false;
//                try {
//                    float diffY = e2.getY() - e1.getY();
//                    float diffX = e2.getX() - e1.getX();
//                    if (Math.abs(diffX) > Math.abs(diffY)) {
//                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
////                            if (diffX > 0) {
////                                onSwipeRight();
////                            } else {
////                                onSwipeLeft();
////                            }
//                            result = true;
//                        }
//                    }
//                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
//                            onSwipeBottom();
//                        } else {
//                            onSwipeTop();
//                        }
//                        result = true;
//                    }
//                }
//                catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//                return result;
//            }
//        }
////        void onSwipeRight() {
////            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
////            this.onSwipe.swipeRight();
////        }
////        void onSwipeLeft() {
////            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
////            this.onSwipe.swipeLeft();
//        }
//        void onSwipeTop() {
//            fc.fold(false);
//            this.onSwipe.swipeTop();
//        }
//        void onSwipeBottom() {
//            fc.unfold(false);
//            this.onSwipe.swipeBottom();
//        }
//        interface onSwipeListener {
//            void swipeRight();
//            void swipeTop();
//            void swipeBottom();
//            void swipeLeft();
//        }
//        onSwipeListener onSwipe;
    }
