package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.plogginglovers.Model.RecyclingManager;

import java.util.ArrayList;

public class FindGarbageActivity extends AppCompatActivity {

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_garbage);

        ListView listViewGarbage = (ListView) findViewById(R.id.listOfGarbage);
        EditText editTextFilter = (EditText) findViewById(R.id.searchFilter);

        adapter = new ArrayAdapter(this, R.layout.activity_find_garbage, RecyclingManager.values());
        listViewGarbage.setAdapter(adapter);

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (FindGarbageActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
