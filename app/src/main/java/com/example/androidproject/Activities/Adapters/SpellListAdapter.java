package com.example.androidproject.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidproject.Database.Model.Spell;
import com.example.androidproject.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpellListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private ArrayList<Spell> spells;

    public SpellListAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;
        this.spells = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return spells.size();
    }

    @Override
    public Spell getItem(int position) {
        return spells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.detail_list, null);
        Spell spell = spells.get(position);

        TextView text = v.findViewById(R.id.detail_text);
        text.setText(spell.name);

        return v;
    }

    public void updateSpells(Collection<Spell> spells) {
        this.spells.clear();
        this.spells.addAll(spells);
        notifyDataSetChanged();
    }
}
