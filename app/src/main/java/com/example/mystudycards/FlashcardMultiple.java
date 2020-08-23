package com.example.mystudycards;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import java.util.List;
import java.util.Random;

import static com.example.mystudycards.R.anim.left_in;
import static com.example.mystudycards.R.anim.left_out;
import static com.example.mystudycards.R.anim.right_in;
import static com.example.mystudycards.R.anim.right_out;

public class FlashcardMultiple extends AppCompatActivity {

    Button test_button, timerBtn, all_deck, deck1_btn, deck2_btn, deck3_btn;
    TextView flashcardQuestion, flashcardAnswer, choice1, choice2, choice3;
    ImageButton add_question, next_button, previous_button, edit_button, delete_button;
    Animation anim_left_out, anim_left_in, anim_right_out, anim_right_in;
    Animator in_animation, out_animation;
    LinearLayout buttons_view, answers_view, empty, deck_btn_view;
    CountDownTimer countDownTimer1, countDownTimer2, countDownTimer3;
    RelativeLayout cardview;
    String title_edit = "Edit Flashcards", setType = "t_multi", setDeck = "d_one";
    int currentCardDisplayedIndex = 0, currentCard = 0, randNum = 0, rightAns = 0, wrongAns = 0;
    boolean emptyState = false, isShowingAns = false, next = false, prev = false, remove = false,
            shuffle = false, timer = false, test = false, home_btn = false, show_all = true;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_multiple);

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
                all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
                deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                show_all=true;

                getFlashCards();
            }
        });

        //DECK 1 button action, this will change buttons view depending on the selected deck
        //This will define in which deck card will be shown
        deck1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
                deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
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
                all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
                deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
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
                all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
                setDeck = "d_three";
                show_all=false;
                getFlashCards();
            }
        });

        //CHOICE 1 button action, this will check if answer is correct or wrong and behave accordingly
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF CHOICE 1 CORRECT
                if(choice1.getText().toString().equals(allFlashcards.get(currentCardDisplayedIndex).getAnswer())) {
                    countDownTimer3 = new CountDownTimer(1000, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //rotate to show correct answer
                            isShowingAns = true;
                            out_animation.setTarget(flashcardQuestion);
                            in_animation.setTarget(flashcardAnswer);
                            out_animation.start();
                            in_animation.start();
                        }
                        @Override
                        public void onFinish() {
                            countDownTimer2 = new CountDownTimer(500, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    //display explode particle animation
                                    new ParticleSystem(FlashcardMultiple.this, 1000,
                                            R.drawable.confetti,700)
                                            .setSpeedRange(0.1f, 0.9f)
                                            .oneShot(findViewById(R.id.choice1), 200);
                                    //change color to GREEN
                                    choice1.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_card_green));
                                }
                                @Override
                                public void onFinish() {
                                    //on countdown Finish reset button color
                                    choice1.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg));
                                    //IF TEST MODE add correct answer to results, and show next card
                                    if (test){
                                        rightAns++;
                                        next_button.performClick();
                                    }
                                }
                            };
                            countDownTimer2.start();
                        }
                    };
                    countDownTimer3.start();
                //IF CHOICE 1 INCORRECT
                }else{
                    countDownTimer2 = new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //change colour to RED
                            choice1.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_card_red));
                        }
                        @Override
                        public void onFinish() {
                            //on countdown Finish reset button color
                            choice1.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg));
                            //IF TEST MODE add correct answer to results, and show next card
                            if(test){
                                wrongAns++;
                                next_button.performClick();
                            }
                        }
                    };
                    countDownTimer2.start();
                }
            }
        });

        //CHOICE 2 button action, this will check if answer is correct or wrong and behave accordingly
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF CHOICE 2 CORRECT
                if(choice2.getText().toString().equals(allFlashcards.get(currentCardDisplayedIndex).getAnswer())) {
                    countDownTimer3 = new CountDownTimer(1000, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //rotate to show correct answer
                            isShowingAns = true;
                            out_animation.setTarget(flashcardQuestion);
                            in_animation.setTarget(flashcardAnswer);
                            out_animation.start();
                            in_animation.start();
                        }
                        @Override
                        public void onFinish() {
                            countDownTimer2 = new CountDownTimer(500, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    //display explode particle animation
                                    new ParticleSystem(FlashcardMultiple.this, 1000,
                                            R.drawable.confetti,700)
                                            .setSpeedRange(0.1f, 0.9f)
                                            .oneShot(findViewById(R.id.choice2), 200);
                                    //change color to GREEN
                                    choice2.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_card_green));
                                }
                                @Override
                                public void onFinish() {
                                    //on countdown Finish reset button color
                                    choice2.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg));
                                    //IF TEST MODE add correct answer to results, and show next card
                                    if (test){
                                        rightAns++;
                                        next_button.performClick();
                                    }
                                }
                            };
                            countDownTimer2.start();
                        }
                    };
                    countDownTimer3.start();
                //IF CHOICE 2 INCORRECT
                }else{
                    countDownTimer2 = new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //change colour to RED
                            choice2.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_card_red));
                        }
                        @Override
                        public void onFinish() {
                            //on countdown Finish reset button color
                            choice2.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg));
                            //IF TEST MODE add correct answer to results, and show next card
                            if(test){
                                wrongAns++;
                                next_button.performClick();
                            }
                        }
                    };
                    countDownTimer2.start();
                }
            }
        });

        //CHOICE 3 button action, this will check if answer is correct or wrong and behave accordingly
        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IF CHOICE 3 CORRECT
                if(choice3.getText().toString().equals(allFlashcards.get(currentCardDisplayedIndex).getAnswer())) {
                    countDownTimer3 = new CountDownTimer(1000, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //rotate to show correct answer
                            isShowingAns = true;
                            out_animation.setTarget(flashcardQuestion);
                            in_animation.setTarget(flashcardAnswer);
                            out_animation.start();
                            in_animation.start();
                        }
                        @Override
                        public void onFinish() {
                            countDownTimer2 = new CountDownTimer(500, 500) {
                                @Override
                                //display explode particle animation
                                public void onTick(long millisUntilFinished) {
                                    new ParticleSystem(FlashcardMultiple.this, 1000,
                                            R.drawable.confetti,700)
                                            .setSpeedRange(0.1f, 0.9f)
                                            .oneShot(findViewById(R.id.choice3), 200);
                                    //change color to GREEN
                                    choice3.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_card_green));
                                }
                                @Override
                                public void onFinish() {
                                    //on countdown Finish reset button color
                                    choice3.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg));
                                    //IF TEST MODE add correct answer to results, and show next card
                                    if (test){
                                        rightAns++;
                                        next_button.performClick();
                                    }
                                }
                            };
                            countDownTimer2.start();
                        }
                    };
                    countDownTimer3.start();
                //IF CHOICE 3 INCORRECT
                }else{
                    countDownTimer2 = new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //change colour to RED
                            choice3.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_card_red));
                        }
                        @Override
                        public void onFinish() {
                            //on countdown Finish reset button color
                            choice3.setBackground(ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg));
                            //IF TEST MODE add correct answer to results, and show next card
                            if(test){
                                wrongAns++;
                                next_button.performClick();
                            }
                        }
                    };
                    countDownTimer2.start();
                }
            }
        });

        //TEST button action, this will initiate or finalise test mode
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override //
            public void onClick(View view) {
                //IF TEST MODE NOT ACTIVE
                if(!test){
                    //AlertDialog to inform user that test mode is active, to finish and get results click resBtn
                    // Build AlertDialog notice
                    AlertDialog.Builder builder = new AlertDialog.Builder(FlashcardMultiple.this);
                    @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.test_mode_alert, null);
                    // Set up view controllers
                    final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
                    Button buttonOK = dialogView.findViewById(R.id.buttonOK);
                    TextView descp = dialogView.findViewById(R.id.descp);
                    builder.setView(dialogView);
                    // Avoid cancellation with back button or when touched outside
                    builder.setCancelable(false);
                    // Create AlertDialog
                    final AlertDialog alertDialog = builder.create();
                    descp.setText("Study the flashcards on your own.\n\nTo finish and get your study results click the button Finish Study Mode.");
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
                            // scale cardview
                            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                            int px = (int) (370 * scale + 0.5f);
                            cardview.getLayoutParams().height = px;
                            //show next card
                            next_button.performClick();
                            countDownTimer1 = new CountDownTimer(16000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    timer = true;
                                    timerBtn.setText("" + millisUntilFinished/1000);
                                }
                                @Override
                                public void onFinish() {
                                    next_button.performClick();
                                    countDownTimer1.start();
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
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();

                //IF TEST MODE ACTIVE
                }else{
                    //AlertDialog to inform user that test mode will be finished, show results
                    // Build AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(FlashcardMultiple.this);
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
                            //scale cardview layout
                            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                            int px = (int) (350 * scale + 0.5f);
                            cardview.getLayoutParams().height = px;

                            timerBtn.setVisibility(View.GONE);
                            buttons_view.setVisibility(View.VISIBLE);
                            deck_btn_view.setVisibility(View.VISIBLE);
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
                        animAnsNext();
                    } else {
                        //displayNextCard in Order Asc
                        nextCardAsc();
                    }
                }
            }
        });

        //PREVIOUS button action, this show PREVIOUS card when clicked
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //if showing ans rotate to show question
                    if (isShowingAns) {
                        prev = true;
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
                    //show Anim flashcardQuestion touch
                    animCardTouch();

                }
            }
        });

        //FLASHCARD ANSWER button action, this will rotate card and show question side
        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_animation.isRunning() || !out_animation.isRunning()) {
                    //show Anim flashcardAnswer touch
                    animCardTouch();
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
                    Intent intent = new Intent(FlashcardMultiple.this, CreateFlashcards.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("HomeBtn", home_btn);
                    bundle.putString("setType", setType);
                    bundle.putString("setDeck", setDeck);
                    //bundle.putString("title", title_add);
                    intent.putExtras(bundle);
                    FlashcardMultiple.this.startActivityForResult(intent, 100);
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
                    //if all get curent card setdeck to show next on create page
                    Log.d("AAAcima", " Toco edit  " + show_all +"" );
                    if(show_all){
                        setDeck = allFlashcards.get(currentCardDisplayedIndex).getDeckNum();
                        Log.d("AAA", " Toco edit  " + setType +  "    setDeck== " + setDeck + ""  );
                    }

                    // Open AddCardActivity - requestCode 110 (edit card)
                    Intent intent = new Intent(FlashcardMultiple.this, CreateFlashcards.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title_edit);
                    bundle.putBoolean("HomeBtn", home_btn);
                    bundle.putString("setType", setType);
                    bundle.putString("setDeck", setDeck);
                    intent.putExtras(bundle);
                    //Get current card inputs to be updated
                    intent.putExtra("currentQuestion", flashcardQuestion.getText());
                    intent.putExtra("currentAnswer", flashcardAnswer.getText());
                    intent.putExtra("answerChoice1", choice1.getText());
                    intent.putExtra("answerChoice2", choice2.getText());
                    intent.putExtra("answerChoice3", choice3.getText());
                    FlashcardMultiple.this.startActivityForResult(intent, 110);
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
                    Toast.makeText(FlashcardMultiple.this,"Flashcard Removed Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method to Update view acording to DECK and displays respective CARDS
    private void getFlashCards() {
        // update view to show all Multi cards
        if (show_all){
            all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
            deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
            deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
            deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));

            allFlashcards = flashcardDatabase.getAllMultiCards();

        }else{
            switch (setDeck) {
                // update view to show all Multi cards Deck1
                case "d_one":
                    all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
                    deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));

                    allFlashcards = flashcardDatabase.getAllMultiCards1();
                    break;
                // update view to show all Multi cards Deck2
                case "d_two":
                    all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));
                    deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));

                    allFlashcards = flashcardDatabase.getAllMultiCards2();
                    break;
                // update view to show all Multi cards Deck3
                case "d_three":
                    all_deck.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck1_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck2_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_lg)));
                    deck3_btn.setBackground((ContextCompat.getDrawable(FlashcardMultiple.this, R.drawable.round_btn_deck)));

                    allFlashcards = flashcardDatabase.getAllMultiCards3();
                    break;
            }
        }

        // IF cards on database show Flashcards
        if(allFlashcards != null && allFlashcards.size() > 0) {
            currentCardDisplayedIndex = 0;

            flashcardQuestion.startAnimation(anim_right_out);
            flashcardQuestion.startAnimation(anim_left_in);
            flashcardAnswer.startAnimation(anim_right_out);
            flashcardAnswer.startAnimation(anim_left_in);

            answers_view.startAnimation(anim_right_out);
            answers_view.startAnimation(anim_left_in);

            //displayCurrentCard
            displayCurrentCard();
        }
        // If NO cards show empty state
        else {
            //displayEmpty
            empty.setVisibility(View.VISIBLE);
            empty.startAnimation(anim_right_out);
            empty.startAnimation(anim_left_in);
            edit_button.setVisibility(View.INVISIBLE);
            delete_button.setVisibility(View.INVISIBLE);
            test_button.setVisibility(View.GONE);
            answers_view.setVisibility(View.GONE);
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
                empty.setVisibility(View.GONE); edit_button.setVisibility(View.VISIBLE); delete_button.setVisibility(View.VISIBLE);
                test_button.setVisibility(View.VISIBLE); answers_view.setVisibility(View.VISIBLE);
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
            displayCurrentCardAnimNext();


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
            displayCurrentCardAnimNext();

            //if Type deck changes inform user that cards was transferred successfully
            setType = new_setType;
            if (setType.equals("t_only")){
                Toast.makeText(FlashcardMultiple.this,"Flashcard Transferred to Only Flashcard Mode Successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(FlashcardMultiple.this,"Flashcard Updated Successfully", Toast.LENGTH_SHORT).show();
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
            displayCurrentCardAnimNext();
        } else { // if no more cards display empty
            emptyState = true;
            //displayEmpty with anim
            empty.setVisibility(View.VISIBLE);
            empty.startAnimation(anim_right_out);
            empty.startAnimation(anim_left_in);
            edit_button.setVisibility(View.INVISIBLE);
            delete_button.setVisibility(View.INVISIBLE);
            test_button.setVisibility(View.GONE);
            answers_view.setVisibility(View.GONE);
        }
    }

    //Method to show PREVIOUS Card
    private void prevCardDsc(){
        //if shuffle active get random card to show if NOT show prev
        if (shuffle) {
            if(allFlashcards.size() > 2){
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
                Toast.makeText(FlashcardMultiple.this,"Reached first card. Going to last", Toast.LENGTH_SHORT).show();
            }else {
                currentCardDisplayedIndex--;
            }
        }
        //displayCurrentCard with anim left
        flashcardQuestion.startAnimation(anim_right_out);
        flashcardQuestion.startAnimation(anim_left_in);
        flashcardAnswer.startAnimation(anim_right_out);
        flashcardAnswer.startAnimation(anim_left_in);
        answers_view.startAnimation(anim_right_out);
        answers_view.startAnimation(anim_left_in);
        //displayCurrentCard
        displayCurrentCard();

        //IF Timer restart Timer
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
                Toast.makeText(FlashcardMultiple.this,"Reached last card. Going to first", Toast.LENGTH_SHORT).show();
            }
        }
        //displayCurrentCard with anim right
        displayCurrentCardAnimNext();
    }

    //Method to Rotate Flashcards if showing Answer before displaying next, previous or removing  card
    private void animAnsNext(){
        setCamDistance();
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
                //IF Next go next
                if(next){
                    next = false;
                    nextCardAsc();
                //IF prev go prev
                }else if (prev){
                    prev =false;
                    prevCardDsc();
                //IF Remove (show next card)
                }else if (remove){
                    remove =false;
                    removeCardShowNex();
                }
                //clear animation list
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

    //Method to show animation when displaying next card
    private void displayCurrentCardAnimNext(){
        //IF Timer reset timer
        if(timer){
            countDownTimer1.start();
        }

        //next card animation
        flashcardQuestion.startAnimation(anim_left_out);
        flashcardQuestion.startAnimation(anim_right_in);
        flashcardAnswer.startAnimation(anim_left_out);
        flashcardAnswer.startAnimation(anim_right_in);
        answers_view.startAnimation(anim_left_out);
        answers_view.startAnimation(anim_right_in);
        //displayCurrentCard
        displayCurrentCard();
    }

    //Method to show back or front of the card when users touch card
    private void animCardTouch(){
        setCamDistance();

        //IF Not showing answer and not test - rotate to show answer
        if(!isShowingAns && !test){
            isShowingAns = true;
            flashcardQuestion.setClickable(false);
            flashcardAnswer.setClickable(true);
            out_animation.setTarget(flashcardQuestion);
            in_animation.setTarget(flashcardAnswer);
            out_animation.start();
            in_animation.start();
        //IF showing anser and not test - rotate to show question
        }else if (isShowingAns && !test) {
            isShowingAns = false;
            flashcardQuestion.setClickable(true);
            flashcardAnswer.setClickable(false);
            out_animation.setTarget(flashcardAnswer);
            in_animation.setTarget(flashcardQuestion);
            out_animation.start();
            in_animation.start();
        }
    }

    //Method to display current card
    private void displayCurrentCard(){
        flashcardQuestion.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
        flashcardAnswer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

        //Get random num from 1 to 3
        int correctPosition = getRandNum(1, 3);
        //IF 1 correctPosition = choice 1
        if(correctPosition == 1) {
            choice1.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            choice2.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            choice3.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        }
        //IF 2 correctPosition = choice 2
        else if(correctPosition == 2) {
            choice1.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            choice2.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            choice3.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        }
        //IF 3 correctPosition = choice 3
        else {
            choice1.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            choice2.setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
            choice3.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
        }

        //Empty state is false hide empty elements
        emptyState = false;
        empty.setVisibility(View.GONE); edit_button.setVisibility(View.VISIBLE);
        delete_button.setVisibility(View.VISIBLE); test_button.setVisibility(View.VISIBLE);
        answers_view.setVisibility(View.VISIBLE);
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
        choice1 = findViewById(R.id.choice1); choice2 =findViewById(R.id.choice2); choice3 =findViewById(R.id.choice3);
        buttons_view = findViewById(R.id.buttons_view);  next_button = findViewById(R.id.next_button);
        previous_button = findViewById(R.id.previous_button);
        add_question = findViewById(R.id.add_question); edit_button = findViewById(R.id.edit_button);
        delete_button = findViewById(R.id.delete_button);
        answers_view = findViewById(R.id.answers_view); cardview = findViewById(R.id.cardview);
        all_deck = findViewById(R.id.all_deck); deck1_btn = findViewById(R.id.deck1_btn); deck2_btn = findViewById(R.id.deck2_btn); deck3_btn = findViewById(R.id.deck3_btn);
        deck_btn_view = findViewById(R.id.deck_btn_view);

        anim_left_in = AnimationUtils.loadAnimation(this, left_in);
        anim_left_out = AnimationUtils.loadAnimation(this, left_out);
        anim_right_in = AnimationUtils.loadAnimation(this, right_in);
        anim_right_out = AnimationUtils.loadAnimation(this, right_out);
        in_animation = AnimatorInflater.loadAnimator(this, R.animator.in_animation);
        out_animation = AnimatorInflater.loadAnimator(this, R.animator.out_animation);
    }
}
