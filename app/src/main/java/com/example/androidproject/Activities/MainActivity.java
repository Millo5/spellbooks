package com.example.androidproject.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidproject.Activities.Adapters.CharacterPagerAdapter;
import com.example.androidproject.Database.AppDatabase;
import com.example.androidproject.Database.Model.Character;
import com.example.androidproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> addCharacterLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), (result) -> {}
    );


    private CharacterPagerAdapter characterPagerAdapter;
    private ViewPager2 pager;
    private boolean loadPagerPage = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Spellbook Archive");

        AppDatabase.initialize(getApplicationContext());

        FloatingActionButton addButton = findViewById(R.id.main_addButton);
        pager = findViewById(R.id.main_pager);


        loadPagerPage = true;
        characterPagerAdapter = new CharacterPagerAdapter(this);
        pager.setAdapter(characterPagerAdapter);

        AppDatabase.getInstance().characterDao().getAllCharactersWithSpellCountLive()
                .observe(this, (characters) ->
                        updateCharacters(characters, savedInstanceState)
                );



        addButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, AddCharacterActivity.class);
            addCharacterLauncher.launch(intent);
        });

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentItem", pager.getCurrentItem());
    }

    private void updateCharacters(List<? extends Character> characters, Bundle savedInstanceState) {
        characterPagerAdapter.updateCharacters(characters);

        // This has to be done after updating the adapter
        if (loadPagerPage) {
            loadPagerPage = false;
            if (savedInstanceState != null) {
                int currentItem = savedInstanceState.getInt("currentItem", 0);
                pager.setCurrentItem(currentItem, false);
            }
        }
    }

}