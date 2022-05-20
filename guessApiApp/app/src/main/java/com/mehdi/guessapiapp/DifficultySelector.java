package com.mehdi.guessapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DifficultySelector extends AppCompatActivity {
    Button easy;
    Button medium;
    Button hard;

    public void difficultyselect(View view) {
        Button button = (Button) view;
        if (button.getText().equals("easy")) {
            Intent intent = new Intent(getApplicationContext(), easyDifficulty.class);
            startActivity(intent);
        }
        if (button.getText().equals("medium")) {
            Intent intent = new Intent(getApplicationContext(), mediumDifficulty.class);
            startActivity(intent);
        }
        if (button.getText().equals("hard")) {
            Intent intent = new Intent(getApplicationContext(), hardDifficulty.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selector);
        easy = (Button) findViewById(R.id.easy);
        medium = (Button) findViewById(R.id.medium);
        hard = (Button) findViewById(R.id.hard);
    }
}