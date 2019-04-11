package com.example.plogginglovers.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plogginglovers.Model.RecyclingManager;
import com.example.plogginglovers.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.core.content.ContextCompat;


public class MyFindGarbageListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> maintitle = new ArrayList<>(); //todo change variable name
    private ArrayList<String> filterListAux = new ArrayList<>();

    public MyFindGarbageListAdapter(Activity context, ArrayList<String> maintitles) {
        super(context, R.layout.my_list, maintitles);
        // TODO Auto-generated constructor stub

        this.context=context;
        maintitle.addAll(maintitles);
        filterListAux.addAll(maintitles);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        System.out.println(titleText.getText().toString());
        String ecoponto = RecyclingManager.INSTANCE.getEcoponto(maintitle.get(position));
        if (ecoponto.equals("Amarelo")){
            titleText.setText(maintitle.get(position));
            imageView.setImageResource(R.drawable.ecoponto_amarelo);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.yellowEcoponto));
        } else if (ecoponto.equals("Azul")){
            titleText.setText(maintitle.get(position));
            imageView.setImageResource(R.drawable.ic_stats_black_24dp);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blueEcoponto));
        } else if (ecoponto.equals("Verde")){
            titleText.setText(maintitle.get(position));
            imageView.setImageResource(R.drawable.ic_stats_black_24dp);
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.greenEcoponto));
        } else{
            titleText.setText(maintitle.get(position));
            imageView.setImageResource(R.drawable.ic_stats_black_24dp);
            imageView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.communEcoponto));
            //textView.setTextColor(ContextCompat.getColor(getContext(), R.color.communEcoponto));
        }

        return rowView;
    }

    @Override
    public int getCount() {
        return maintitle.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        maintitle.clear();
        if (charText.length() == 0) {
            maintitle.addAll(filterListAux);
        }
        else
        {
            for (String wp : filterListAux)
            {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    maintitle.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
