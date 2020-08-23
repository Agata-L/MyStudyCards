package com.example.mystudycards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
//CREATE FLASHCARDS PAGE logic operations, here users can create two types of cards,that can be saved in one of three decks
public class CreateFlashcards extends AppCompatActivity {

    CheckBox check_only, check_multi;
    Button deck1_btn, deck2_btn, deck3_btn;
    EditText edit_question, edit_answer, edit_incorrect_1, edit_incorrect_2;
    TextView title;
    ImageView cancel_add_card, save_question;
    LinearLayout incorrect_lyt, question_lyt;
    String setType = "t_only", setDeck = "d_one";
    Boolean home_btn = true;
    String currentQuestion, currentAnswer, answerChoice1, answerChoice2, answerChoice3,
            questionText, answerText, incorrect1Text, incorrect2Text;

    FlashcardDatabase flashcardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcards);

        setupControls();
        setupButtons();
        getBundles();

        //Initiate Database
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        updateview();

    }

    // Method to Update View according to Card Type and Deck Num, this will also get input in case of Edit
    private void updateview() {
        //Change card Type
        switch (setType){
            case "t_only":
                check_only.setChecked(true);
                check_multi.setChecked(false);
                incorrect_lyt.setVisibility(View.GONE);
                final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                question_lyt.getLayoutParams().height = (int) (270 * scale + 0.5f);
                break;
            case "t_multi":
                check_multi.setChecked(true);
                check_only.setChecked(false);
                incorrect_lyt.setVisibility(View.VISIBLE);
                question_lyt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        //Change deck Type
        switch (setDeck) {
            case "d_one":
                deck1_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_deck)));
                deck2_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                break;
            case "d_two":
                deck1_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_deck)));
                deck3_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                break;
            case "d_three":
                deck1_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_deck)));
                break;
        }

        //Get current Question and Answer Inputs for Edit
        currentQuestion = getIntent().getStringExtra("currentQuestion");
        currentAnswer = getIntent().getStringExtra("currentAnswer");
        //set current Question and Answer inputs
        edit_question.setText(currentQuestion);
        edit_answer.setText(currentAnswer);
        //Get current AnswerChoices Inputs for Edit
        answerChoice1 = getIntent().getStringExtra("answerChoice1");
        answerChoice2 = getIntent().getStringExtra("answerChoice2");
        answerChoice3 = getIntent().getStringExtra("answerChoice3");
        //set current AnswerChoices input
        if(answerChoice1 != null && answerChoice1.equals(currentAnswer)) {
            edit_incorrect_1.setText(answerChoice2);
            edit_incorrect_2.setText(answerChoice3);
        }
        else if(answerChoice2 != null && answerChoice2.equals(currentAnswer)) {
            edit_incorrect_1.setText(answerChoice1);
            edit_incorrect_2.setText(answerChoice3);
        }
        else if(answerChoice3 != null && answerChoice3.equals(currentAnswer)) {
            edit_incorrect_1.setText(answerChoice1);
            edit_incorrect_2.setText(answerChoice2);
        }
    }

    // Method to get Bundles of Page
    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        title.setText(bundle.getString("title", "Create Flashcards"));home_btn = bundle.getBoolean("HomeBtn");
        setType = bundle.getString("setType", "t_multi"); setDeck = bundle.getString("setDeck", "d_one");
    }

    // Method for Buttons Actions
    private void setupButtons() {
        //CANCEL button action, when users clicks button activity is closed
        cancel_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //SAVE button action, when users clicks SAVE this will validate text fields and insert new flashcard if user came from Home page
        // or generate result code 100 to use onActivityResult of Modes Pages
        save_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text fields inputs
                questionText = edit_question.getText().toString();
                answerText = edit_answer.getText().toString();
                incorrect1Text = edit_incorrect_1.getText().toString();
                incorrect2Text = edit_incorrect_2.getText().toString();

                //Validate inputs for Only Cards, show Toast if not valid
                if(setType.equals("t_only") && (questionText.equals("") || answerText.equals(""))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Question and Answer Needed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0,20);
                    toast.show();
                //Validate inputs for Muti Cards, show Toast if not valid
                } else if (setType.equals("t_multi") && (questionText.equals("") || answerText.equals("") || incorrect1Text.equals("") || incorrect2Text.equals(""))){
                    Toast toast = Toast.makeText(getApplicationContext(), "Question, Answer and Incorrect Needed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0,20);
                    toast.show();

                } else {
                    //If user access this page NOT from Home Page (i.e. User come from one of Study Modes)
                    if (!home_btn){
                        //Create new Intent and push Input Data to ResultCode 100
                        Intent data = new Intent();

                        String new_setType = setType;
                        String new_setDeck = setDeck;

                        data.putExtra("new_setType", new_setType );
                        data.putExtra("new_setDeck", new_setDeck );
                        data.putExtra("question", questionText);
                        data.putExtra("answer", answerText);
                        data.putExtra("incorrect1", incorrect1Text);
                        data.putExtra("incorrect2", incorrect2Text);

                        setResult(100, data);

                        //Log.d("DDD", "Save Create new_setType==  " + new_setType + "    new_setDeck== " + new_setDeck + ""  );
                        finish();
                    }else{
                        //If user access this page from Home Page get Inputs Insert them into DataBase and reset View
                        questionText = edit_question.getText().toString();
                        answerText = edit_answer.getText().toString();
                        incorrect1Text= edit_incorrect_1.getText().toString();
                        incorrect2Text = edit_incorrect_2.getText().toString();
                        String typeCard =  setType;
                        String deckNum =  setDeck;
                        //Insert Inputs into DB
                        flashcardDatabase.insertCard(new Flashcard(questionText, answerText, incorrect1Text, incorrect2Text, typeCard, deckNum));
                        //Reset View
                        edit_question.setText(""); edit_answer.setText(""); edit_incorrect_1.setText(""); edit_incorrect_2.setText("");
                        //Inform operation Success
                        Toast toast = Toast.makeText(getApplicationContext(), "Flashcard Created Successfully!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0,20);
                        toast.show();
                    }
                }
            }
        });

        //CHECK ONLY button action, this will change view depending on type of card
        check_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF ONLY CARD show two fields, Question and Answer
                if(check_only.isChecked()){
                    check_multi.setChecked(false);
                    incorrect_lyt.setVisibility(View.GONE);
                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    question_lyt.getLayoutParams().height = (int) (270 * scale + 0.5f);
                    setType = "t_only";
                //IF MULTI CARD show four fields, Question and Answer and two Incorrect Answers
                }else{
                    check_multi.setChecked((true));
                    incorrect_lyt.setVisibility(View.VISIBLE);
                    question_lyt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    setType = "t_multi";
                }
            }
        });

        //CHECK MULTI button action, this will change view depending on type of card
        check_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF MULTI CARD show four fields, Question and Answer and two Incorrect Answers
                if(check_multi.isChecked()){
                    check_only.setChecked(false);
                    incorrect_lyt.setVisibility(View.VISIBLE);
                    question_lyt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    setType = "t_multi";
                //IF ONLY CARD show two fields, Question and Answer
                }else{
                    check_only.setChecked(true);
                    incorrect_lyt.setVisibility(View.GONE);
                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    question_lyt.getLayoutParams().height = (int) (270 * scale + 0.5f);
                    setType = "t_only";
                }
            }
        });


        //DECK 1 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be saved
        deck1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deck1_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_deck)));
                deck2_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                setDeck = "d_one";
            }
        });

        //DECK 2 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be saved
        deck2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deck1_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_deck)));
                deck3_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                setDeck = "d_two";
            }
        });

        //DECK 3 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be saved
        deck3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deck1_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(CreateFlashcards.this, R.drawable.round_btn_deck)));
                setDeck = "d_three";
            }
        });
    }

    //Method to Initialise Controllers of the page
    private void setupControls() {
        check_only = findViewById(R.id.check_only); check_multi = findViewById(R.id.check_multi);
        deck1_btn = findViewById(R.id.deck1_btn); deck2_btn = findViewById(R.id.deck2_btn); deck3_btn = findViewById(R.id.deck3_btn);
        edit_question = findViewById(R.id.edit_question); edit_answer = findViewById(R.id.edit_answer);
        edit_incorrect_1 = findViewById(R.id.edit_incorrect_1); edit_incorrect_2 = findViewById(R.id.edit_incorrect_2);
        incorrect_lyt = findViewById(R.id.incorrect_lyt); question_lyt = findViewById(R.id.question_lyt);
        cancel_add_card = findViewById(R.id.cancel_add_card); save_question = findViewById(R.id.save_question);
        title = findViewById(R.id.title);
    }
}
