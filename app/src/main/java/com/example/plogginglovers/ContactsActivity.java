package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plogginglovers.Adapters.ContactsListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    private ArrayList<Contact> dataModels;
    private ListView listView;
    private static ContactsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        listView=(ListView)findViewById(R.id.contactsList);

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<List<Contact>> call = service.getAllNumbers();

        //Execute the request asynchronously//
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                System.out.println(response.body());
                // Add a marker in Sydney and move the camera
                dataModels = new ArrayList<>();

                if (response.body() != null) {
                    dataModels.addAll(response.body());

                    adapter = new ContactsListAdapter(dataModels, getApplicationContext());

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Contact contact = dataModels.get(position);

                            AlertDialog dialogBuilder = new AlertDialog.Builder(ContactsActivity.this).create();
                            //new MaterialDialog(getApplicationContext());
                            LayoutInflater inflater = ContactsActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.contact_info_dialog, null);
                            dialogBuilder.setTitle("Call " + contact.getName() + " ?");
                            dialogBuilder.setIcon(R.drawable.call);
                            dialogBuilder.setButton(DialogInterface.BUTTON_POSITIVE, "Call", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Toast.makeText(getApplicationContext(), "Calling...", Toast.LENGTH_LONG).show();
                                }
                            });

                            dialogBuilder.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            TextView txtNumber = (TextView) dialogView.findViewById(R.id.txtNumber);
                            TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);

                            txtNumber.setText(String.valueOf(contact.getNumber()));
                            txtDescription.setText(contact.getDescription());

                            dialogBuilder.setView(dialogView);
                            dialogBuilder.show();
                        }
                    });
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<List<Contact>> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(ContactsActivity.this, "Unable to load contacts", Toast.LENGTH_SHORT).show();
            }
        });

        /*
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
        */


    }


    public static Intent getIntent(Context context){
        return new Intent(context, ContactsActivity.class);
    }
}
