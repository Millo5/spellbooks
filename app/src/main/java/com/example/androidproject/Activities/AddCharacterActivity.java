package com.example.androidproject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.Database.AppDatabase;
import com.example.androidproject.Database.Model.Character;
import com.example.androidproject.R;
import com.example.androidproject.Util.ActivityUtil;
import com.example.androidproject.Util.ImageUtil;

import java.io.IOException;
import java.util.UUID;

public class AddCharacterActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::onSelectImageResult
    );


    private ImageView image;
    private Uri imageUri;
    private int characterId;
    private Character character;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_character);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Add Character");
        ActivityUtil.enableUpButton(this);

        characterId = getIntent().getIntExtra("characterId", -1);

        image = findViewById(R.id.add_image);
        Button addButton = findViewById(R.id.add_buttonAdd);
        Button imageButton = findViewById(R.id.add_buttonImage);
        EditText nameField = findViewById(R.id.add_inputName);

        if (characterId >= 0) {
            AppDatabase.runAsync((db) -> {
                character = db.characterDao().getCharacter(characterId);
                if (character == null) return;

                runOnUiThread(() -> {
                    nameField.setText(character.name);
                    imageUri = ImageUtil.getUriFromPath(character.imageUri);
                    image.setImageURI(imageUri);
                    addButton.setText("Save");
                });
            });
        }


        imageButton.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            selectImageLauncher.launch(intent);
        });

        addButton.setOnClickListener((v) -> {
            boolean valid = true;
            if (imageUri == null) {
                imageButton.setError("Image cannot be empty");
                valid = false;
            }

            String name = nameField.getText().toString();
            String error = isNameValid(name);
            if (error != null) {
                nameField.setError(error);
                valid = false;
            }

            if (!valid) return;

            try {
                String path = ImageUtil.copyUriToInternalStorage(this, imageUri, UUID.randomUUID().toString());

                AppDatabase.runAsync((db) -> {
                    if (characterId >= 0) {
                        character.update(name, path);
                        db.characterDao().updateCharacter(character);
                    } else {
                        Character character = new Character(name, path);
                        db.characterDao().insertCharacter(character);
                    }

                    Intent intent = new Intent();
                    intent.putExtra("characterName", name);
                    setResult(RESULT_OK, intent);
                    finish();
                });
            } catch (IOException e) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private String isNameValid(String name) {
        if (name.isEmpty()) return "Name cannot be empty";
        if (name.length() > 32) return "Name cannot be longer than 32 characters";
        if (name.matches(".*[\n\\r].*")) return "Name cannot contain new lines";
        return null;
    }

    private void onSelectImageResult(ActivityResult result) {
        if (result.getResultCode() != RESULT_OK) return;

        Intent data = result.getData();
        if (data == null) return;

        imageUri = data.getData();
        if (imageUri != null) {
            image.setImageURI(imageUri);
        }

    }
}