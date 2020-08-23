package com.example.mystudycards;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

//Create DAOs, an interface with annotation @Dao which will define the all the necessary methods and queries to access the database.
@Dao
public interface FlashcardDao {
    //Get all cards
    @Query("SELECT * FROM flashcard" )
    List<Flashcard> getAll();
    //Get all type ONLY cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_only' " )
    List<Flashcard> getAllOnly();
    //Get all type ONLY DECK 1 cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_only' AND deck_num='d_one' " )
    List<Flashcard> getAllOnlyDeck1();
    //Get all type ONLY DECK 2 cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_only' AND deck_num='d_two' " )
    List<Flashcard> getAllOnlyDeck2();
    //Get all type ONLY DECK 2 cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_only' AND deck_num='d_three'" )
    List<Flashcard> getAllOnlyDeck3();
    //Get all type MULTI cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_multi' " )
    List<Flashcard> getAllMuti();
    //Get all type MULTI DECK 1 cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_multi' AND deck_num='d_one' " )
    List<Flashcard> getAllMultiDeck1();
    //Get all type MULTI DECK 2 cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_multi' AND deck_num='d_two' " )
    List<Flashcard> getAllMultiDeck2();
    //Get all type MULTI DECK 3 cards
    @Query("SELECT * FROM flashcard WHERE type_card='t_multi' AND deck_num='d_three'" )
    List<Flashcard> getAllMultiDeck3();

    //Insert, delete, and update operations
    @Insert
    void insertAll(Flashcard... flashcards);

    @Delete
    void delete(Flashcard flashcard);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Flashcard flashcard);
}
