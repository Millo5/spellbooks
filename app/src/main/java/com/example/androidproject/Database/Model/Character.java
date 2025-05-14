package com.example.androidproject.Database.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Entity
public class Character {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String imageUri;


    public Character(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }

    public void update(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }



    @Dao
    public interface CharacterDao {
        @Query("SELECT * FROM character")
        List<Character> getAllCharacters();

        @Query("SELECT * FROM character")
        LiveData<List<Character>> getAllCharactersLive();

        @Insert
        void insertCharacter(Character character);

        @Update
        void updateCharacter(Character character);

        @Delete
        void deleteCharacter(Character character);

        @Query("SELECT character.*, COUNT(spell.id) AS spellCount FROM character LEFT JOIN spell ON character.id = spell.characterId GROUP BY character.id")
        LiveData<List<CharacterWithSpellCount>> getAllCharactersWithSpellCountLive();

        @Query("SELECT * FROM character WHERE id = :characterId")
        Character getCharacter(int characterId);
    }


}
