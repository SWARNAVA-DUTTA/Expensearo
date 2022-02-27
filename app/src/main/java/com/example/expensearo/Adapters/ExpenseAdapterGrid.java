package com.example.expensearo.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ExpenseAdapterGrid extends FirebaseRecyclerAdapter<Expense,ExpenseAdapterGrid.myviewholder>
{
    Context mContext;
    TextView cart_item_name,cart_item_description,cart_item_price;
    ImageView cart_item_image;
    Button yes_btn,no_btn;
    public ExpenseAdapterGrid(FirebaseRecyclerOptions<Expense> options, Context mContext) {
        super(options);
        this.mContext=mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ExpenseAdapterGrid.myviewholder holder, final int position, @NonNull final Expense model)
    {

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final String currentUserId=mAuth.getCurrentUser().getUid();


        Picasso.get().load(model.getIcon()).into(holder.logo);

    }

    @NonNull
    @Override
    public ExpenseAdapterGrid.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid_recycler_layout,parent,false);
        return new myviewholder(view);
    }
    class myviewholder extends RecyclerView.ViewHolder
    {
        public TextView txtTitle;

        public ImageView logo;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            logo = (ImageView) itemView.findViewById(R.id.logo);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mclicklistener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }
    private ExpenseAdapterGrid.OnItemClickListener mclicklistener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(ExpenseAdapterGrid.OnItemClickListener listener)
    {
        mclicklistener=listener;
    }
}
