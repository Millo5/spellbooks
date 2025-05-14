package com.example.androidproject.Database;

import androidx.room.TypeConverter;

import com.example.androidproject.Util.Response.SpellData;
import com.google.gson.Gson;

public class Converters {

    @TypeConverter
    public String fromSpellData(SpellData spellData) {
        return new Gson().toJson(spellData);
    }

    @TypeConverter
    public SpellData toSpellData(String spellDataString) {
        return new Gson().fromJson(spellDataString, SpellData.class);
    }

}
