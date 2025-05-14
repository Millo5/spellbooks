package com.example.androidproject.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.Database.AppDatabase;
import com.example.androidproject.Database.Model.Spell;
import com.example.androidproject.R;
import com.example.androidproject.Util.Response.SpellData;
import com.google.gson.Gson;

public class SpellDetailsActivity extends ActivityWithBackButton {

    public static class SearchViewModel extends ViewModel {
        public Spell spell;
    }


    private SearchViewModel viewModel;
    private boolean isInSpellBook = false;
    private Button button;
    private TextView descriptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spell_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        descriptionView = findViewById(R.id.spellDetails_desc);
        button = findViewById(R.id.spellDetails_button);

        String name = getIntent().getStringExtra("spellName");
        String spellIndex = getIntent().getStringExtra("spellIndex");
        int characterId = getIntent().getIntExtra("characterId", -1);

        if (name == null || spellIndex == null || characterId == -1) {
            finish();
            return;
        }

        setTitle(name);

        button.setClickable(false);

        AppDatabase.runAsync((db) -> {
            isInSpellBook = db.spellDao().getSpellsByCharacterId(characterId).stream().anyMatch(s -> {
                if (s.index.equals(spellIndex)) {
                    viewModel.spell = s;
                    return true;
                }
                return false;
            });

            runOnUiThread(() -> {
                updateButton();
                if (viewModel.spell == null) {
                    requestSpellData(spellIndex, descriptionView, characterId, name);
                } else {
                    updateDescription();
                    button.setClickable(true);
                }
            });
        });

        button.setOnClickListener((v) -> {
            if (viewModel.spell == null) return;
            button.setClickable(false);

            AppDatabase.runAsync((db) -> {
                if (isInSpellBook) {
                    db.spellDao().deleteSpell(viewModel.spell);
                    isInSpellBook = false;
                } else {
                    db.spellDao().insertSpell(viewModel.spell);
                    isInSpellBook = true;
                }
                runOnUiThread(() -> {
                    button.setClickable(true);
                    updateButton();
                });
            });
        });

    }

    private void requestSpellData(String spellIndex, TextView descriptionView, int characterId, String name) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.dnd5eapi.co/api/spells/" + spellIndex;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            SpellData spellData = new Gson().fromJson(response, SpellData.class);
            viewModel.spell = new Spell(characterId, spellIndex, name, spellData);

            button.setClickable(true);
            updateDescription();
        }, error -> {
            descriptionView.setText("Error loading spell data");
            button.setClickable(false);
        });
        queue.add(request);
    }

    private void updateButton() {
        if (isInSpellBook) {
            button.setText("Remove from Spellbook");
        } else {
            button.setText("Add to Spellbook");
        }
    }

    private void updateDescription() {
        if (viewModel.spell != null && viewModel.spell.spellData != null) {
            descriptionView.setText(viewModel.spell.spellData.getDescription());
            return;
        }
        descriptionView.setText("Loading...");
    }

}