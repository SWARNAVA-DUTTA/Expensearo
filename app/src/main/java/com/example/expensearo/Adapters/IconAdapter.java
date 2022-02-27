package com.example.expensearo.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensearo.JavaClasses.IconModel;
import com.example.expensearo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class IconAdapter extends FirebaseRecyclerAdapter<IconModel,IconAdapter.myviewholder>
{
    private int selectedItem=-1;
    private Context context;

    public IconAdapter(FirebaseRecyclerOptions<IconModel> options,Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull IconModel model)
    {
        Picasso.get().load(model.getImage()).into(holder.icon);
//        if (selectedItem == position) {
//            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
//        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int previousItem = selectedItem;
//                selectedItem = position;
//                notifyItemChanged(previousItem);
//                notifyItemChanged(position);
//
//
//
//            }
//        });

    }
    @NonNull
    @Override
    public IconAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_icon_layout,parent,false);
        return new myviewholder(view);

    }



    class myviewholder extends RecyclerView.ViewHolder
    {
        public ImageView icon;
        public myviewholder(@NonNull final View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mclicklistener.onItemClick(v,getAdapterPosition());


                }
            });
        }
    }
    private OnItemClickListener mclicklistener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mclicklistener=listener;
    }
}
