package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.plogginglovers.Adapters.ContactsListAdapter;
import com.example.plogginglovers.Model.Contact;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ArrayList<Contact> dataModels;
    private ListView listView;
    private static ContactsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        listView=(ListView)findViewById(R.id.contactsList);

        dataModels = new ArrayList<>();

        dataModels.add(new Contact("First Contact", 123456789, "sadlasdl"));
        dataModels.add(new Contact("Second Contact", 123456789, "sadlasdl1"));
        dataModels.add(new Contact("Third Contact", 123456789, "sadlasdl2"));
        dataModels.add(new Contact("Fourth Contact", 123456789, "sadlasdl3"));
        dataModels.add(new Contact("Fifth Contact", 123456789, "sadlasdl4"));
        dataModels.add(new Contact("Sixth Contact", 123456789, "sadlasdl5"));
        dataModels.add(new Contact("Seventh Contact", 123456789, "sadlasdl6"));
        dataModels.add(new Contact("Eighth Contact", 123456789, "sadlasdl7"));
        dataModels.add(new Contact("Ninth Contact", 123456789, "sadlasdl8"));
        dataModels.add(new Contact("Tenth Contact", 123456789, "sadlasdl9"));
        dataModels.add(new Contact("Eleventh Contact", 123456789, "sadlasdl10"));
        dataModels.add(new Contact("Twelfth Contact", 123456789, "sadlasdl11"));
        dataModels.add(new Contact("Thirteenth Contact", 123456789, "sadlasdl12"));

        adapter= new ContactsListAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = dataModels.get(position);

                Toast.makeText(getApplicationContext(), "Description " + contact.getDescription(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public static Intent getIntent(Context context){
        return new Intent(context, ContactsActivity.class);
    }
}
