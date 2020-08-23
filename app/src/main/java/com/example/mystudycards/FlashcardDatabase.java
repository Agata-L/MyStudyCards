package com.example.mystudycards;

import android.content.Context;

import androidx.room.Room;

import java.util.List;
//Create FlashcardDatabase class, here all database operation methods are defined with the DAOs
class FlashcardDatabase {
    private final AppDatabase db;

    FlashcardDatabase(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "flashcard-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    List<Flashcard> getAllCards() {
        return db.flashcardDao().getAll();
    }

    List<Flashcard> getAllOnlyCards() { return db.flashcardDao().getAllOnly();}
    List<Flashcard> getAllOnlyCards1() { return db.flashcardDao().getAllOnlyDeck1();}
    List<Flashcard> getAllOnlyCards2() { return db.flashcardDao().getAllOnlyDeck2();}
    List<Flashcard> getAllOnlyCards3() { return db.flashcardDao().getAllOnlyDeck3();}

    List<Flashcard> getAllMultiCards() { return db.flashcardDao().getAllMuti();}
    List<Flashcard> getAllMultiCards1() { return db.flashcardDao().getAllMultiDeck1();}
    List<Flashcard> getAllMultiCards2() { return db.flashcardDao().getAllMultiDeck2();}
    List<Flashcard> getAllMultiCards3() { return db.flashcardDao().getAllMultiDeck3();}

    void insertCard(Flashcard flashcard) {
        db.flashcardDao().insertAll(flashcard);
    }

    void deleteCard(String flashcardQuestion) {
        List<Flashcard> allCards = db.flashcardDao().getAll();
        for (Flashcard f : allCards) {
            if (f.getQuestion().equals(flashcardQuestion)) {
                db.flashcardDao().delete(f);
            }
        }
    }

    void updateCard(Flashcard flashcard) {
        db.flashcardDao().update(flashcard);
    }
}