package com.example.expensearo.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.JavaClasses.Expense;
import com.example.expensearo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ExpenseAdapter extends FirebaseRecyclerAdapter<Expense,ExpenseAdapter.myviewholder>
{
    Context mContext;

    public ExpenseAdapter(FirebaseRecyclerOptions<Expense> options, Context mContext) {
        super(options);
        this.mContext=mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Expense model)
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final String currentUserId=mAuth.getCurrentUser().getUid();

        holder.txtTitle.setText(model.getName());
        Picasso.get().load(model.getIcon()).into(holder.logo);
    }
    @NonNull
    @Override
    public ExpenseAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
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
    private ExpenseAdapter.OnItemClickListener mclicklistener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(ExpenseAdapter.OnItemClickListener listener)
    {
        mclicklistener=listener;
    }
}
