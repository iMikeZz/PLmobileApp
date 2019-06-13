package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.plogginglovers.Model.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Message> adapter;

    private EditText input;
    private FloatingActionButton fab;

    private SharedPreferences pref;

    private String activity_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //mudar cor da status bar
        //----------------------
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.blue_cenas_escuro));
        //-----------------

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Chat");

        input = (EditText)findViewById(R.id.input);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        activity_id = String.valueOf(getIntent().getExtras().getInt("id"));

        final String studentName = pref.getString("studentName", null);

        Toast.makeText(this, "Bem vindo " + studentName, Toast.LENGTH_LONG).show();

        // Load chat room contents
        displayChatMessages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                if (!input.getText().toString().trim().equals("")) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(activity_id)
                            .push()
                            .setValue(new Message(studentName,
                                            input.getText().toString())
                            );

                    // Clear the input
                    input.setText("");
                }
            }
        });
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child(activity_id)) {

            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessage());
                messageUser.setText(model.getNickname());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Intent getIntent(Context context){
        return new Intent(context, ChatActivity.class);
    }
}
