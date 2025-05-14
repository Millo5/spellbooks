package com.example.androidproject.Database.Model;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.example.androidproject.Util.Response.SpellData;

import java.util.List;

@Entity(
    foreignKeys = @ForeignKey(
        entity = Character.class,
        parentColumns = "id",
        childColumns = "characterId",
        onDelete = ForeignKey.CASCADE
    ))
public class Spell {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int characterId;

    public String name;
    public String index;

    @Nullable
    public SpellData spellData;



    public Spell(int characterId, String index, String name, SpellData spellData) {
        this.characterId = characterId;
        this.index = index;
        this.name = name;
        this.spellData = spellData;
    }

    @Ignore
    public Spell(String index, String name) {
        this.index = index;
        this.name = name;
    }



    @Dao
    public interface SpellDao {

        @Insert
        void insertSpell(Spell spell);

        @Query("SELECT * FROM spell WHERE characterId = :characterId")
        LiveData<List<Spell>> getSpellsByCharacterIdLive(int characterId);


        @Query("SELECT * FROM spell WHERE characterId = :characterId")
        List<Spell> getSpellsByCharacterId(int characterId);

        @Delete
        void deleteSpell(Spell spell);

        @Query("DELETE FROM spell WHERE id = :spellIndex")
        void deleteSpellByIndex(String spellIndex);

        @Query("SELECT * FROM spell WHERE characterId = :characterId AND spell.`index` = :spellIndex")
        Spell getSpell(int characterId, String spellIndex);
    }

}
