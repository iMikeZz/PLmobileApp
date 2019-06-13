package com.example.plogginglovers.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.plogginglovers.Model.RecyclingManager;
import com.example.plogginglovers.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import io.github.tonnyl.light.Light;


public class MyFindGarbageListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> garbageList = new ArrayList<>();
    private ArrayList<String> filterListAux = new ArrayList<>();

    public MyFindGarbageListAdapter(Activity context, ArrayList<String> garbagesList) {
        super(context, R.layout.my_list, garbagesList);
        this.context=context;
        garbageList.addAll(garbagesList);
        filterListAux.addAll(garbagesList);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_list, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        System.out.println(titleText.getText().toString());
        final String ecoponto = RecyclingManager.INSTANCE.getEcoponto(garbageList.get(position));
        if (ecoponto.equals("Amarelo")) {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_amarelo);
        } else if (ecoponto.equals("Azul")) {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_azul);
        } else if (ecoponto.equals("Verde")) {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_verde);
        } else if (ecoponto.equals("Pilhão")) {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_verde);
            imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.terminated_activity_red));
        } else if (ecoponto.equals("Ecocentro")) {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_verde);
            imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
        } else if (ecoponto.equals("Farmácia")) {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.ecoponto_verde);
            imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.purpleEcoponto));
        } else {
            titleText.setText(garbageList.get(position));
            imageView.setImageResource(R.drawable.lixo_comum);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ecoponto.equals("Amarelo") || ecoponto.equals("Verde") || ecoponto.equals("Azul")) {
                    Light.make(v, "Ecoponto " + ecoponto, R.drawable.trash_can, R.color.material_grey /*backgroung*/, R.color.white /*textColor*/, Snackbar.LENGTH_SHORT).show();
                } else if (ecoponto.equals("Comum")) {
                    Light.make(v, "Lixo " + ecoponto, R.drawable.trash_can, R.color.material_grey /*backgroung*/, R.color.white /*textColor*/, Snackbar.LENGTH_SHORT).show();
                } else {
                    Light.make(v, ecoponto, R.drawable.trash_can, R.color.material_grey /*backgroung*/, R.color.white /*textColor*/, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

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

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
