package com.example.androidproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.Activities.Adapters.SpellListAdapter;
import com.example.androidproject.Database.Model.Spell;
import com.example.androidproject.R;
import com.example.androidproject.Util.ActivityUtil;
import com.example.androidproject.Util.Response.SpellListResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SpellSearchActivity extends ActivityWithBackButton {

    public static class SearchViewModel extends ViewModel {
        public ArrayList<Spell> spells = null;
    }

    private SearchViewModel viewModel;

    private int characterId;
    private SpellListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spell_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Spell Search");
        ActivityUtil.enableUpButton(this);


        characterId = getIntent().getIntExtra("characterId", -1);
        if (characterId == -1) {
            finish();
            return;
        }

        adapter = new SpellListAdapter(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        if (viewModel.spells != null) {
            adapter.updateSpells(viewModel.spells);
        }

        ListView listView = findViewById(R.id.spellSearch_list);
        TextInputEditText searchField = findViewById(R.id.spellSearch_searchText);
        ImageButton searchButton = findViewById(R.id.spellSearch_searchButton);
        listView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        searchButton.setOnClickListener((v) -> {
            String searchText = searchField.getText() == null ? "" : searchField.getText().toString().trim();
            if (searchText.isEmpty()) return;

            String url = "https://www.dnd5eapi.co/api/spells/?name=" + searchText;

            StringRequest req = new StringRequest(Request.Method.GET, url, (response) -> {
                SpellListResponse spellListResponse = new Gson().fromJson(response, SpellListResponse.class);

                ArrayList<Spell> spells = new ArrayList<>();
                for (SpellListResponse.SpellListElement spell : spellListResponse.getResults()) {
                    spells.add(new Spell(spell.getIndex(), spell.getName()));
                }

                adapter.updateSpells(spells);
                viewModel.spells = spells;
            }, (err) -> System.out.println("Error: " + err.getMessage()));

            queue.add(req);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Spell spell = adapter.getItem(position);

            Intent intent = new Intent(this, SpellDetailsActivity.class);
            intent.putExtra("spellIndex", spell.index);
            intent.putExtra("spellName", spell.name);
            intent.putExtra("characterId", characterId);
            startActivity(intent);
        });
    }

}