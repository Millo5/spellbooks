package com.example.androidproject.Activities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Activities.SpellListActivity;
import com.example.androidproject.Database.Model.Character;
import com.example.androidproject.Database.Model.CharacterWithSpellCount;
import com.example.androidproject.R;
import com.example.androidproject.Util.ImageUtil;

import java.util.ArrayList;
import java.util.Collection;

public class CharacterPagerAdapter extends RecyclerView.Adapter<CharacterPagerAdapter.CharacterPagerViewHolder> {

    ArrayList<Character> characters = new ArrayList<>();

    Context context;
    Activity parent;

    public CharacterPagerAdapter(AppCompatActivity parent) {
        this.parent = parent;
        this.context = parent.getApplicationContext();
    }

    public void updateCharacters(Collection<? extends Character> characters) {
        this.characters.clear();
        this.characters.addAll(characters);
        notifyDataSetChanged();
    }


    public static class CharacterPagerViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        Button button;
        TextView name;

        public CharacterPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_image);
            button = itemView.findViewById(R.id.item_openButton);
            name = itemView.findViewById(R.id.item_name);
        }
    }


    @NonNull
    @Override
    public CharacterPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(context).inflate(R.layout.item_character, parent, false);
        return new CharacterPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterPagerViewHolder holder, int position) {
        Character character = characters.get(position);

        holder.img.setImageURI(ImageUtil.getUriFromPath(character.imageUri));
        holder.name.setText(character.name);
        if (character instanceof CharacterWithSpellCount) {
            CharacterWithSpellCount spellCount = (CharacterWithSpellCount) character;
            if (spellCount.spellCount > 0) {
                String text = character.name + " (" + spellCount.spellCount + ")";
                holder.name.setText(text);
            }
        }

        holder.button.setOnClickListener((v) -> {
            Intent intent = new Intent(context, SpellListActivity.class);
            intent.putExtra("characterId", character.id);

            parent.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }
}
