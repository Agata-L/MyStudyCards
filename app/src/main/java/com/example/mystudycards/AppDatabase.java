package com.example.mystudycards;

import androidx.room.Database;
import androidx.room.RoomDatabase;
//Create APPDATABASE by defining abstract class that extends to RoomDatabase, annotated with @Database then define the DAOs
@Database(entities = {Flashcard.class},exportSchema = false, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FlashcardDao flashcardDao();
}