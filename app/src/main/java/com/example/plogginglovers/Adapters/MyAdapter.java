package com.example.plogginglovers.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.plogginglovers.Model.RetroUser;
import com.example.plogginglovers.R;

import java.util.List;

//Extend the RecyclerView.Adapter class//

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private List<RetroUser> dataList;

    public MyAdapter(List<RetroUser> dataList){
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        //Get a reference to the Views in our layout//
        public final View myView;

        TextView textUser;

        CustomViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            textUser = myView.findViewById(R.id.user);
        }
    }

    @Override
    //Construct a RecyclerView.ViewHolder//
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    //Set the data//
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.textUser.setText(dataList.get(position).getName());
    }

    //Calculate the item count for the RecylerView//
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}