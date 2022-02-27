package com.example.expensearo.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.JavaClasses.MonthlyBudget;
import com.example.expensearo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MonthlyBudgetAdapter extends FirebaseRecyclerAdapter<MonthlyBudget,MonthlyBudgetAdapter.myviewholder>
{
    Context mContext;

    public MonthlyBudgetAdapter(FirebaseRecyclerOptions<MonthlyBudget> options, Context mContext) {
        super(options);
        this.mContext=mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull MonthlyBudget model)
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final String currentUserId=mAuth.getCurrentUser().getUid();

        holder.title.setText(model.getType());
        holder.note.setText(model.getNote());
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        Picasso.get().load(model.getIcon()).into(holder.logo);

    }
    @NonNull
    @Override
    public MonthlyBudgetAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_monthly_budget,parent,false);
        return new myviewholder(view);
    }



    class myviewholder extends RecyclerView.ViewHolder
    {
        public TextView title,note,amount,date;
        public ImageView logo;


        public myviewholder(@NonNull final View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            note = (TextView)itemView.findViewById(R.id.note);
            amount = (TextView)itemView.findViewById(R.id.amount);
            date = (TextView)itemView.findViewById(R.id.date);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mclicklistener.onItemClick(v,getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    mlongclicklistener.onItemLongClick(v,getAdapterPosition());
                    return true;
                }
            });
        }
    }
    private MonthlyBudgetAdapter.OnItemClickListener mclicklistener;
    private MonthlyBudgetAdapter.OnItemLongClickListener mlongclicklistener;
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(MonthlyBudgetAdapter.OnItemClickListener listener)
    {
        mclicklistener=listener;
    }
    public interface OnItemLongClickListener
    {
        void onItemLongClick(View view, int position);
    }
    public void setOnItemLongClickListener(MonthlyBudgetAdapter.OnItemLongClickListener listener)
    {
        mlongclicklistener=listener;
    }
}
