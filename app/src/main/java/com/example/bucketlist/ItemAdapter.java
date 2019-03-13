package com.example.bucketlist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.content_rvrow, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(row);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.ViewHolder viewHolder, int i) {
        final Item item = itemList.get(i);
        (viewHolder).titleView.setText(item.getTitle());
        (viewHolder).descriptionView.setText(item.getDescription());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.titleView.setPaintFlags(viewHolder.titleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.descriptionView.setPaintFlags(viewHolder.descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    viewHolder.titleView.setPaintFlags(viewHolder.titleView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.descriptionView.setPaintFlags(viewHolder.descriptionView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void swapList (List<Item> newList) {
        itemList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView titleView;
        TextView descriptionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            titleView = itemView.findViewById(R.id.titleView);
            descriptionView = itemView.findViewById(R.id.descriptionView);


        }
    }
}
