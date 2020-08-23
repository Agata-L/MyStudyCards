package com.example.mystudycards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//HOME PAGE logic operations to implement menu buttons
public class MainActivity extends AppCompatActivity {

    Button onlyFlashcardBtn, multiFlashcardBtn, helpBtn, aboutBtn, contactsBtn, create_btn;
    Boolean home_btn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupControllers();
        setupButtons();
    }

    // Method for Buttons Actions
    private void setupButtons() {
        //CREATE button action, this will use INTENT to send users to CreateFlashcards activity Page
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_flashcard = new Intent(MainActivity.this,CreateFlashcards.class);
                //Bundle to identify user comes from HOME PAGE
                Bundle bundle = new Bundle();
                bundle.putBoolean("HomeBtn", home_btn);
                create_flashcard.putExtras(bundle);
                startActivity(create_flashcard);
            }
        });

        //ONLY FLASHCARD button action, this will use INTENT to send users to Only Flashcard Mode activity Page
        onlyFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent only_flashcard = new Intent(MainActivity.this,OnlyCardActivity.class);
                //Bundle to identify user comes from HOME PAGE
                Bundle bundle = new Bundle();
                bundle.putBoolean("HomeBtn", home_btn);
                only_flashcard.putExtras(bundle);
                startActivity(only_flashcard);
            }
        });

        //MULTI FLASHCARD button action, this will use INTENT to send users to Multi Flashcard Mode activity Page
        multiFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent flashcard_multiple = new Intent(MainActivity.this, FlashcardMultiple.class);
                //Bundle to identify user comes from HOME PAGE
                Bundle bundle = new Bundle();
                bundle.putBoolean("HomeBtn", home_btn);
                flashcard_multiple.putExtras(bundle);
                startActivity(flashcard_multiple);
            }
        });

        //HELP button action, this will use INTENT to send users to Help activity Page
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_help = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(i_help);
            }
        });

        //ABOUT button action, this will use INTENT to send users to About activity Page
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_about = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(i_about);
            }
        });

        //Contacts button action, this will use INTENT to send users to Contacts activity Page
        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_contacts = new Intent(MainActivity.this,ContactsActivity.class);
                startActivity(i_contacts);
            }
        });
    }

    //Method to Initialise Controllers of the page
    private void setupControllers() {
        onlyFlashcardBtn =findViewById(R.id.onlyFlashcardBtn);
        multiFlashcardBtn = findViewById(R.id.multiFlashcardBtn);
        helpBtn = findViewById(R.id.helpBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
        contactsBtn = findViewById(R.id.contactsBtn);
        create_btn = findViewById(R.id.create_btn);
    }
}
