package com.example.mystudycards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//CONTACTS PAGE operations to implement form to send email to developer
public class ContactsActivity extends AppCompatActivity {
    EditText name, email, subject, message;
    Button send;
    boolean onError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        setupControls();
        setupButtons();

    }

    // Method for Buttons Actions
    private void setupButtons() {
        //SEND button action, when users clicks SEND text fields are validated and email can be sent to developer
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user inputs from text fields
                String name = ContactsActivity.this.name.getText().toString();
                String email = ContactsActivity.this.email.getText().toString();
                String subject = ContactsActivity.this.subject.getText().toString();
                String message = ContactsActivity.this.message.getText().toString();

                //Validate NAME using TextUtils
                if (TextUtils.isEmpty(name)){
                    ContactsActivity.this.name.setError("Enter Your Name");
                    ContactsActivity.this.name.requestFocus();
                    return;
                }
                onError = false;
                //Validate EMAIL using isValidEmail Method
                if (!isValidEmail(email)) {
                    onError = true;
                    ContactsActivity.this.email.setError("Invalid Email");
                    return;
                }
                //Validate SUBJECT using TextUtils
                if (TextUtils.isEmpty(subject)){
                    ContactsActivity.this.subject.setError("Enter Your Subject");
                    ContactsActivity.this.subject.requestFocus();
                    return;

                }
                //Validate MESSAGE using TextUtils
                if (TextUtils.isEmpty(message)){
                    ContactsActivity.this.message.setError("Enter Your Message");
                    ContactsActivity.this.message.requestFocus();
                    return;
                }
                // Create new INTENT to send new EMAIL
                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

                // Push form fields data to email
                sendEmail.setType("plain/text");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"s15001436@mail.glyndwr.ac.uk"});
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Name: "+name+'\n'+"Email ID: "+email+'\n'+"Message: "+'\n'+message);

                // Send it off to the Activity-Chooser
                startActivity(Intent.createChooser(sendEmail, "Send mail..."));
            }

        });
    }

    //Method to Initialise Controllers of the page
    private void setupControls() {

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
    }

    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    //Method to validating email
    private boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }


}
