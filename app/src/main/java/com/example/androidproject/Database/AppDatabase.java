package com.example.androidproject.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidproject.Database.Model.Character;
import com.example.androidproject.Database.Model.Spell;

@Database(entities = {com.example.androidproject.Database.Model.Character.class, Spell.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        return INSTANCE;
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "characters.db")
                    .allowMainThreadQueries() // Allow main thread queries for simplicity
                    .build();
        }
    }


    public abstract Character.CharacterDao characterDao();
    public abstract Spell.SpellDao spellDao();



    public static void runAsync(DatabaseRunnable runnable) {
        new Thread(() -> {
            AppDatabase db = getInstance();
            runnable.run(db);
        }).start();
    }

    public interface DatabaseRunnable {
        void run(AppDatabase db);
    }

}
