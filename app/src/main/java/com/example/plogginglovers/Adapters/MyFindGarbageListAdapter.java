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
    private final ArrayList<String> garbageList = new ArrayList<>();
    private ArrayList<String> filterListAux = new ArrayList<>();

    public MyFindGarbageListAdapter(Activity context, ArrayList<String> maintitles) {
        super(context, R.layout.my_list, maintitles);
        this.context=context;
        garbageList.addAll(maintitles);
        filterListAux.addAll(maintitles);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        System.out.println(titleText.getText().toString());
        String ecoponto = RecyclingManager.INSTANCE.getEcoponto(garbageList.get(position));
        if (ecoponto.equals("Amarelo")){
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_amarelo);
        } else if (ecoponto.equals("Azul")){
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_azul);
        } else if (ecoponto.equals("Verde")){
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_verde);
        } else{
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.lixo_comum);
        }

        return rowView;
    }

    @Override
    public int getCount() {
        return garbageList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        garbageList.clear();
        if (charText.length() == 0) {
            garbageList.addAll(filterListAux);
        }
        else
        {
            for (String garbage : filterListAux)
            {
                if (garbage.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    garbageList.add(garbage);
                }
            }
        }
        notifyDataSetChanged();
    }
}
