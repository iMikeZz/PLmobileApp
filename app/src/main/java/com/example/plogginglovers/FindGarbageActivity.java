package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.plogginglovers.Adapters.MyFindGarbageListAdapter;
import com.example.plogginglovers.Model.RecyclingManager;

import java.util.ArrayList;
import java.util.Locale;

public class FindGarbageActivity extends AppCompatActivity {

    private MyFindGarbageListAdapter adapter;
    private EditText editTextFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_garbage);

        ListView listViewGarbage = (ListView) findViewById(R.id.listOfGarbage);
        editTextFilter = (EditText) findViewById(R.id.searchFilter);


        adapter = new MyFindGarbageListAdapter(this, RecyclingManager.INSTANCE.getGarbagesWithoutEcopontos());

        /*
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, RecyclingManager.INSTANCE.getGarbages()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                String ecoponto = RecyclingManager.INSTANCE.getEcoponto(textView.getText().toString());
                if (ecoponto.equals("Amarelo")){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.yellowEcoponto));
                } else if (ecoponto.equals("Azul")){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blueEcoponto));
                } else if (ecoponto.equals("Verde")){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.greenEcoponto));
                } else{
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.communEcoponto));
                }
                return textView;
            }
        };
        */
        listViewGarbage.setAdapter(adapter);

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(editTextFilter.getText().toString().toLowerCase(Locale.getDefault()));
                //(FindGarbageActivity.this).adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public static Intent getIntent(Context context){
        return new Intent(context, FindGarbageActivity.class);
    }
}
