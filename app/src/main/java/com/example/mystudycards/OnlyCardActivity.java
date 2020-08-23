package com.example.mystudycards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import static com.example.mystudycards.R.anim.left_in;
import static com.example.mystudycards.R.anim.left_out;
import static com.example.mystudycards.R.anim.right_in;
import static com.example.mystudycards.R.anim.right_out;

public class OnlyCardActivity extends AppCompatActivity {

    Button test_button, timerBtn, all_deck, deck1_btn, deck2_btn, deck3_btn;
    TextView flashcardQuestion, flashcardAnswer, rightBtn, wrongBtn;
    ImageButton next_button, previous_button, add_question, edit_button, delete_button;
    Animation anim_left_out, anim_left_in , anim_right_out,  anim_right_in;
    Animator in_animation, out_animation;
    LinearLayout buttons_view, ansBtnsView, empty, deck_btn_view;
    CountDownTimer countDownTimer1, countDownTimer2;
    String title_edit = "Edit Flashcards", setType = "t_only", setDeck = "d_one";
    int currentCardDisplayedIndex = 0, currentCard = 0, randNum = 0, rightAns = 0, wrongAns = 0;
    boolean emptyState = false, isShowingAns = false, next = false, prev = false,
            remove = false, shuffle = false, timer = false, test = false, ans = true, home_btn = false, show_all = true;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_card);

        getBundles();

        //Initiate Database
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        setupControllers();
        setupButtons();
        getFlashCards();

    }

    //Method for Buttons Actions
    private void setupButtons() {

        //DECK ALL button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be shown
        all_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
                deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                show_all=true;

                getFlashCards();
            }
        });

        //DECK 1 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be shown
        deck1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
                deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                setDeck = "d_one";
                show_all=false;
                getFlashCards();
            }
        });

        //DECK 2 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be shown
        deck2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
                deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                setDeck = "d_two";
                show_all=false;
                getFlashCards();
            }
        });

        //DECK 3 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be shown
        deck3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
                setDeck = "d_three";
                show_all=false;

                getFlashCards();
            }
        });

        //Right button action, this will register is user knew the answer
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer2 = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        rightBtn.setBackground(ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_card_green_lg));
                    }
                    @Override
                    public void onFinish() {
                        rightBtn.setBackground(ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_card_green));
                        //show next card and get right value for results
                        next_button.performClick();
                        rightAns++;
                    }
                };
                countDownTimer2.start();
            }
        });

        //Right button action, this will register is user knew the answer
        wrongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer2 = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        wrongBtn.setBackground(ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_card_red_lg));
                    }
                    @Override
                    public void onFinish() {
                        wrongBtn.setBackground(ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_red));
                        //show next card and get right value for results
                        next_button.performClick();
                        wrongAns++;
                    }
                };
                countDownTimer2.start();
            }
        });

        //TEST button action, this will initiate or finalise test mode
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!test){
                    //AlertDialog to inform user that test mode is active, to finish and get results click resBtn
                    // Build AlertDialog notice
                    AlertDialog.Builder builder = new AlertDialog.Builder(OnlyCardActivity.this);
                    @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.test_mode_alert, null);
                    // Set up view controllers
                    final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
                    Button buttonOK = dialogView.findViewById(R.id.buttonOK);
                    builder.setView(dialogView);
                    // Avoid cancellation with back button or when touched outside
                    builder.setCancelable(false);
                    // Create AlertDialog
                    final AlertDialog alertDialog = builder.create();
                    // Button "OK" Action - Initiate TEST MODE
                    buttonOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            test = true;
                            shuffle = true;
                            test_button.setText("Finish Study Mode");
                            timerBtn.setVisibility(View.VISIBLE);
                            buttons_view.setVisibility(View.GONE);
                            deck_btn_view.setVisibility(View.GONE);
                            // show next card
                            next_button.performClick();
                            countDownTimer1 = new CountDownTimer(16000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    timer = true;
                                    timerBtn.setText("" + millisUntilFinished/1000);
                                }
                                @Override
                                public void onFinish() {
                                    flashcardQuestion.performClick();
                                }
                            };
                            alertDialog.dismiss();
                            //start Countdown timer
                            countDownTimer1.start();
                        }
                    });
                    // Button "Cancel" Action - Cancel TEST MODE
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            test = false;
                            next_button.performClick();
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();

                //IF TEST MODE ACTIVE
                }else{
                    //AlertDialog to inform user that test mode will be finished, show results
                    // Build AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(OnlyCardActivity.this);
                    @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.results_alert, null);
                    // Set up view controllers
                    final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
                    Button buttonOK = dialogView.findViewById(R.id.buttonOK);
                    TextView correctRate = dialogView.findViewById(R.id.correctRate);
                    TextView descp = dialogView.findViewById(R.id.descp);
                    builder.setView(dialogView);
                    // Avoid cancellation with back button or when touched outside
                    builder.setCancelable(false);
                    // Create AlertDialog
                    final AlertDialog alertDialog = builder.create();

                    //get right and wrong sum to show study session results
                    int sum = (rightAns + wrongAns);
                    if (sum == 0){
                        //if sum 0 no attempts made
                        int rate = 0;
                        correctRate.setText(rate + "%");
                        descp.setText(("No Attempts have been made yet. \n\n Would you like to try again?"));

                    }else{
                        //if sum > 0 get results rate (percentage)
                        int rate = (rightAns * 100)/(sum);
                        correctRate.setText(rate + "%");
                        descp.setText(("In " + sum + " attempts you got: \n" + rightAns + " Correct. \n" + wrongAns + " Wrong. \n Would you like to try again?"));
                    }

                    // Button "OK" Action - reset right and wrong, and start new TEST study session
                    buttonOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rightAns = 0; wrongAns = 0;
                            test = true;
                            next_button.performClick();
                            alertDialog.dismiss();
                        }
                    });

                    // Button "Cancel" Action - Finish TEST MODE
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            test = false;
                            shuffle = false; timer = false;
                            test_button.setText("Start Study Mode");
                            timerBtn.setVisibility(View.GONE);
                            ansBtnsView.setVisibility(View.GONE);
                            deck_btn_view.setVisibility(View.VISIBLE);
                            buttons_view.setVisibility(View.VISIBLE);
                            countDownTimer1.cancel();
                            next_button.performClick();
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        //NEXT button action, this show NEXT card when clicked
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //if showing ans rotate to show question
                    if (isShowingAns) {
                        next = true;
                        //set up Camera distance
                        setCamDistance();
                        animAnsNext();
                    } else {
                        //displayNextCard in Order Asc
                        nextCardAsc();
                    }
                    if(ans){
                        ansBtnsView.setVisibility(View.GONE);
                        previous_button.setClickable(true);
                    }

                }
            }
        });

        // Previous_button Action
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //if showing ans rotate to show question
                    if (isShowingAns) {
                        prev = true;
                        //set up Camera distance
                        setCamDistance();
                        animAnsNext();
                    } else {
                        //displayPreviousCard in Order Desc
                        prevCardDsc();
                    }
                }
            }
        });

        //FLASHCARD QUESTION button action, this will rotate card and show answer side
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //set up Camera distance
                    setCamDistance();
                    //show Anim flashcardQuestion touch
                    animQuestionTouch();

                    if(test){
                        ansBtnsView.setVisibility(View.VISIBLE);
                        previous_button.setClickable(false);
                        ans = true;
                    }
                }
            }
        });

        //FLASHCARD ANSWER button action, this will rotate card and show question side
        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //set up Camera distance
                    setCamDistance();
                    //show Anim flashcardAnswer touch
                    animAnsTouch();

                    if(test){
                        ansBtnsView.setVisibility(View.GONE);
                        previous_button.setClickable(true);
                        ans = false;
                    }
                }
            }
        });

        //ADD button action, this will open Create page to add new card using onActivityResult method
        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //if showing ans rotate to show question
                    if (isShowingAns) {
                        flashcardAnswer.performClick();
                    }

                    //testing
                    Log.d("AAAcima1", " Toco edit  " + show_all +"" );
                    //IF show all set new card to be added with default deck one
                    if(show_all){
                        setDeck = "d_one";
                        show_all=false;
                        //testing
                        Log.d("AAA1", " Toco edit  " + setType +  "    setDeck== " + setDeck + ""  );
                    }
                    // Open AddCardActivity - requestCode 100 (add card)
                    Intent intent = new Intent(OnlyCardActivity.this, CreateFlashcards.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("HomeBtn", home_btn);
                    bundle.putString("setType", setType);
                    bundle.putString("setDeck", setDeck);
                    intent.putExtras(bundle);
                    OnlyCardActivity.this.startActivityForResult(intent, 100);
                    overridePendingTransition(R.anim.left_in, right_out);

                    Log.d("DDD", " Toco add OnlyCard setType==  " + setType +  "    setDeck== " + setDeck + ""  );
                }
            }
        });

        //EDIT button action, this will open Create page to Edit card using onActivityResult method
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //if showing ans rotate to show question
                    if (isShowingAns) {
                        flashcardAnswer.performClick();
                    }

                    //if all get curent card setdeck
                    Log.d("AAAcima", " Toco edit  " + show_all +"" );
                    if(show_all){
                        setDeck = allFlashcards.get(currentCardDisplayedIndex).getDeckNum();
                        Log.d("AAA", " Toco edit  " + setType +  "    setDeck== " + setDeck + ""  );
                    }

                    // Open AddCardActivity - requestCode 110 (edit card)
                    Intent intent = new Intent(OnlyCardActivity.this, CreateFlashcards.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title_edit);
                    bundle.putBoolean("HomeBtn", home_btn);
                    bundle.putString("setType", setType);
                    bundle.putString("setDeck", setDeck);
                    intent.putExtras(bundle);
                    //Get current card inputs to be updated
                    intent.putExtra("currentQuestion", flashcardQuestion.getText());
                    intent.putExtra("currentAnswer", flashcardAnswer.getText());
                    OnlyCardActivity.this.startActivityForResult(intent, 110);
                    overridePendingTransition(R.anim.right_in, left_out);
                }
            }
        });

        //DELETE button action, this will delete card from DB and show next
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //if showing ans rotate to show question
                    if (isShowingAns) {
                        remove = true;
                        animAnsNext();
                    } else {
                        removeCardShowNex();
                    }

                    Toast.makeText(OnlyCardActivity.this,"Flashcard Removed Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method to Update view acording to DECK and displays respective CARDS
    private void getFlashCards() {
        // update view to show all Multi cards
        if (show_all){
            all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
            deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
            deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
            deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));

            allFlashcards = flashcardDatabase.getAllOnlyCards();

        }else{
            switch (setDeck) {
                // update view to show all Multi cards Deck1
                case "d_one":
                    all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
                    deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));

                    allFlashcards = flashcardDatabase.getAllOnlyCards1();
                    break;
                // update view to show all Multi cards Deck2
                case "d_two":
                    all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));
                    deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));

                    allFlashcards = flashcardDatabase.getAllOnlyCards2();
                    break;
                // update view to show all Multi cards Deck2
                case "d_three":
                    all_deck.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck1_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck2_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_lg)));
                    deck3_btn.setBackground((ContextCompat.getDrawable(OnlyCardActivity.this, R.drawable.round_btn_deck)));

                    allFlashcards = flashcardDatabase.getAllOnlyCards3();
                    break;
            }
        }

        // IF cards on database show Flashcards
        if(allFlashcards != null && allFlashcards.size() > 0) {
           currentCardDisplayedIndex = 0;
            //displayCurrentCard
            flashcardQuestion.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
            flashcardAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            flashcardQuestion.startAnimation(anim_right_out);
            flashcardQuestion.startAnimation(anim_left_in);
            flashcardAnswer.startAnimation(anim_right_out);
            flashcardAnswer.startAnimation(anim_left_in);
            emptyState = false;
            empty.setVisibility(View.GONE); edit_button.setVisibility(View.VISIBLE);
            delete_button.setVisibility(View.VISIBLE); test_button.setVisibility(View.VISIBLE);
        }
        // If NO cards show empty state
        else {
            //displayEmpty
            empty.setVisibility(View.VISIBLE);
            edit_button.setVisibility(View.INVISIBLE);
            delete_button.setVisibility(View.INVISIBLE);
            empty.startAnimation(anim_right_out);
            empty.startAnimation(anim_left_in);
            test_button.setVisibility(View.GONE);
            emptyState = true;
        }
    }

    // Method for onActivityResult, this will open Create page to ADD or EDIT cards
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            //When EmptyState TRUE set emptycardview gone, put edit and delete buttons visible
            if (emptyState) {
                empty.setVisibility(View.GONE); edit_button.setVisibility(View.VISIBLE);
                delete_button.setVisibility(View.VISIBLE); test_button.setVisibility(View.VISIBLE);
            }

            //Get user input for card question and answer, insert new card into database
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");
            String incorrect_1 = data.getExtras().getString("incorrect1");
            String incorrect_2 = data.getExtras().getString("incorrect2");


            String new_setType =  data.getExtras().getString("new_setType");
            String new_setDeck =  data.getExtras().getString("new_setDeck");

            //testing
            Log.d("DDD", "antes insert OnlyCard new_setType==  " + new_setType + "    new_setDeck== " + new_setDeck + ""  );

            //insert new card into DB
            flashcardDatabase.insertCard(new Flashcard(question, answer, incorrect_1, incorrect_2, new_setType, new_setDeck));

            //set DECK to update view accordingly
            setDeck = new_setDeck;
            getFlashCards();
            // Set currentCardDisplayedIndex
            currentCardDisplayedIndex = allFlashcards.size() - 1;
            //displayCurrentCard with anim
            displayCurrentCardAnim();
            if(timer){
                countDownTimer1.start();
            }


        } else if (requestCode == 110 && resultCode == 100) {
            //Get user input for card question and answer, change card
            String new_setType =  data.getExtras().getString("new_setType");
            String new_setDeck =  data.getExtras().getString("new_setDeck");
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");
            String incorrect_1 = data.getExtras().getString("incorrect1");
            String incorrect_2 = data.getExtras().getString("incorrect2");
            flashcardQuestion.setText(question);
            flashcardAnswer.setText(answer);
            //update card into database
            Flashcard curr = allFlashcards.get(currentCardDisplayedIndex);
            curr.setTypeCard(new_setType);
            curr.setDeckNum(new_setDeck);
            curr.setQuestion(question);
            curr.setAnswer(answer);
            curr.setWrongAnswer1(incorrect_1);
            curr.setWrongAnswer2(incorrect_2);
            flashcardDatabase.updateCard(curr);

            //set DECK to update view accordingly
            setDeck = new_setDeck;
            getFlashCards();

            currentCardDisplayedIndex = allFlashcards.size() - 1;
            //displayCurrentCard with anim
            displayCurrentCardAnim();

            //if Type deck changes inform user that cards was transferred successfully
            setType = new_setType;
            if (setType.equals("t_multi")){
                Toast.makeText(OnlyCardActivity.this,"Flashcard Transferred to Multi Flashcard Mode Successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(OnlyCardActivity.this,"Flashcard Updated Successfully", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //Method to show NEXT card after Remove
    private void removeCardShowNex(){
        // delete current card
        flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcardQuestion)).getText().toString());
        allFlashcards.remove(currentCardDisplayedIndex);
        // if still cards display next
        if (allFlashcards.size() > 0) {
            if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                currentCardDisplayedIndex = 0;
            }
            //displayCurrentCard with anim
            displayCurrentCardAnim();
        } else { // if no more cards display empty
            emptyState = true;
            //displayEmpty with anim
            empty.setVisibility(View.VISIBLE);
            empty.startAnimation(anim_right_out);
            empty.startAnimation(anim_left_in);
            edit_button.setVisibility(View.INVISIBLE);
            test_button.setVisibility(View.GONE);
            delete_button.setVisibility(View.INVISIBLE);
            emptyState = true;
        }
    }

    //Method to show PREVIOUS Card
    private void prevCardDsc(){
        //if shuffle active get random card to show if NOT show prev
        if (shuffle) {

            if(allFlashcards.size() > 2 ){
                currentCard = currentCardDisplayedIndex;

                while (randNum == currentCard) {
                    randNum = getRandNum(0, allFlashcards.size() - 1);
                }

                currentCardDisplayedIndex = randNum;
            }else{
                if (currentCardDisplayedIndex == 0) {
                    currentCardDisplayedIndex = allFlashcards.size() - 1;
                }else {
                    currentCardDisplayedIndex--;
                }
            }
        }else{
            //displayPreviousCard in Order Desc
            if (currentCardDisplayedIndex == 0) {
                // show last card when reach the first card
                currentCardDisplayedIndex = allFlashcards.size() - 1;
                Toast.makeText(OnlyCardActivity.this,"Reached first card. Going to last", Toast.LENGTH_SHORT).show();
            }else {
                currentCardDisplayedIndex--;
            }
        }
        //displayCurrentCard with anim left
        flashcardQuestion.startAnimation(anim_right_out);
        flashcardQuestion.startAnimation(anim_left_in);
        flashcardAnswer.startAnimation(anim_right_out);
        flashcardAnswer.startAnimation(anim_left_in);
        flashcardQuestion.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
        flashcardAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

        if(timer){
            countDownTimer1.start();
        }
    }

    //Method to show NEXT Card
    private void nextCardAsc(){
        //if shuffle active get random card to show if NOT show next
        if (shuffle ) {
            if(allFlashcards.size() > 2){
                currentCard = currentCardDisplayedIndex;

                while (randNum == currentCard) {
                    randNum = getRandNum(0, allFlashcards.size() - 1);
                }

                currentCardDisplayedIndex = randNum;
            }else{
                if (currentCardDisplayedIndex < allFlashcards.size() - 1) {
                    currentCardDisplayedIndex++;
                }else {// show first card when reach the end
                    currentCardDisplayedIndex = 0;
                }
            }
        }else{
            //displayNextCard in Order Asc
            if (currentCardDisplayedIndex < allFlashcards.size() - 1) {
                currentCardDisplayedIndex++;
            }else {// show first card when reach the end
                currentCardDisplayedIndex = 0;
                Toast.makeText(OnlyCardActivity.this,"Reached last card. Going to first", Toast.LENGTH_SHORT).show();
            }
        }
        //displayCurrentCard with anim
        displayCurrentCardAnim();
    }

    //Method to Rotate Flashcards if showing Answer before displaying next, previous or removing  card
    private void animAnsNext(){
        isShowingAns = false;
        flashcardQuestion.setClickable(true);
        flashcardAnswer.setClickable(false);
        out_animation.setTarget(flashcardAnswer);
        in_animation.setTarget(flashcardQuestion);
        in_animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //IF next go next
                if(next){
                    next = false;
                    nextCardAsc();
                //IF prev go prev
                }else if (prev){
                    prev =false;
                    prevCardDsc();
                //IF remove show next
                }else if (remove){
                    remove =false;
                    removeCardShowNex();
                }
                //remove animation listener
                in_animation.removeListener(this);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        out_animation.start();
        in_animation.start();
    }

    //Method to show answer when touch
    private void animQuestionTouch(){
        isShowingAns = true;
        flashcardQuestion.setClickable(false);
        flashcardAnswer.setClickable(true);
        out_animation.setTarget(flashcardQuestion);
        in_animation.setTarget(flashcardAnswer);
        out_animation.start();
        in_animation.start();
    }

    //Method to show question when touch
    private void animAnsTouch(){
        isShowingAns = false;
        flashcardQuestion.setClickable(true);
        flashcardAnswer.setClickable(false);
        out_animation.setTarget(flashcardAnswer);
        in_animation.setTarget(flashcardQuestion);
        out_animation.start();
        in_animation.start();
    }

    //Method to display current card
    private void displayCurrentCardAnim(){
        //IF timer restart timer
        if(timer){
            countDownTimer1.start();
        }

        flashcardQuestion.startAnimation(anim_left_out);
        flashcardQuestion.startAnimation(anim_right_in);
        flashcardAnswer.startAnimation(anim_left_out);
        flashcardAnswer.startAnimation(anim_right_in);
        flashcardQuestion.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
        flashcardAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
        emptyState = false;
    }

    //Method to change Camera Distance
    private void setCamDistance(){
        int dist = 20000;
        float scale = getResources().getDisplayMetrics().density * dist;
        flashcardQuestion.setCameraDistance(scale);
        flashcardAnswer.setCameraDistance(scale);
    }

    //Method to get random number
    public int getRandNum(int minNum, int maxNum) {
        Random rand = new Random();
        return rand.nextInt((maxNum - minNum) + 1) + minNum;
    }

    //Method to get Bundles of Page
    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        show_all = bundle.getBoolean("HomeBtn");
        setDeck = bundle.getString("setDeck", "d_one" );
    }

    //Method to Initialise Controllers of the page
    private void setupControllers() {

        test_button = findViewById(R.id.test_button); timerBtn = findViewById(R.id.timerBtn); empty = findViewById(R.id.empty);
        flashcardQuestion = findViewById(R.id.flashcardQuestion); flashcardAnswer =findViewById(R.id.flashcardAnswer);
        next_button = findViewById(R.id.next_button); previous_button = findViewById(R.id.previous_button);
        add_question = findViewById(R.id.add_question); edit_button = findViewById(R.id.edit_button);
        delete_button = findViewById(R.id.delete_button); rightBtn = findViewById(R.id.rightBtn); wrongBtn = findViewById(R.id.wrongBtn);
        buttons_view = findViewById(R.id.buttons_view); ansBtnsView = findViewById(R.id.ansBtnsView);
        all_deck = findViewById(R.id.all_deck);

        deck1_btn = findViewById(R.id.deck1_btn); deck2_btn = findViewById(R.id.deck2_btn); deck3_btn = findViewById(R.id.deck3_btn);
        deck_btn_view = findViewById(R.id.deck_btn_view);

        anim_left_in = AnimationUtils.loadAnimation(this, left_in);
        anim_left_out = AnimationUtils.loadAnimation(this, left_out);
        anim_right_in = AnimationUtils.loadAnimation(this, right_in);
        anim_right_out = AnimationUtils.loadAnimation(this, right_out);
        in_animation = AnimatorInflater.loadAnimator(this, R.animator.in_animation);
        out_animation = AnimatorInflater.loadAnimator(this, R.animator.out_animation);

    }
}
