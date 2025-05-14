package com.example.androidproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.Activities.Adapters.SpellListAdapter;
import com.example.androidproject.Database.AppDatabase;
import com.example.androidproject.Database.Model.Character;
import com.example.androidproject.Database.Model.Spell;
import com.example.androidproject.R;
import com.example.androidproject.Util.ActivityUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SpellListActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> editCharacterLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::onEditCharacterResult
    );


    private Character character;
    private int characterId;
    private SpellListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spell_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityUtil.enableUpButton(this);


        characterId = getIntent().getIntExtra("characterId", -1);
        if (characterId == -1) {
            finish();
            return;
        }

        ListView listView = findViewById(R.id.spellList_list);
        FloatingActionButton addButton = findViewById(R.id.spellList_addButton);

        adapter = new SpellListAdapter(getLayoutInflater());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Spell spell = adapter.getItem(position);
            if (spell == null) return;

            Intent intent = new Intent(this, SpellDetailsActivity.class);
            intent.putExtra("spellName", spell.name);
            intent.putExtra("spellIndex", spell.index);
            intent.putExtra("characterId", characterId);
            startActivity(intent);

        });

        addButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, SpellSearchActivity.class);
            intent.putExtra("characterId", characterId);
            startActivity(intent);
        });

        AppDatabase.runAsync((db) -> {
            character = db.characterDao().getCharacter(characterId);
            if (character == null) {
                finish();
                return;
            }

            runOnUiThread(() -> setTitle(character.name));
        });

        AppDatabase.getInstance().spellDao().getSpellsByCharacterIdLive(characterId)
                .observe(this, adapter::updateSpells);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spell_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Character")
                    .setMessage("Are you sure you want to delete this character? This action cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        AppDatabase.runAsync((db) -> {
                            db.characterDao().deleteCharacter(character);
                            finish();
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        if (id == R.id.menu_edit) {
            Intent intent = new Intent(this, AddCharacterActivity.class);
            intent.putExtra("characterId", characterId);
            editCharacterLauncher.launch(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onEditCharacterResult(ActivityResult activityResult) {
        if (activityResult.getResultCode() != RESULT_OK) return;

        Intent data = activityResult.getData();
        if (data == null) return;

        String name = data.getStringExtra("characterName");
        if (name == null) return;

        character.name = name;
        setTitle(name);
    }
}
