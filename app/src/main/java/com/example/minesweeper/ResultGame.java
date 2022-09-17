package com.example.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra("com.example.minesweeper.MESSAGE");
        //message = message;
        System.out.println(message);

        TextView textView = (TextView) findViewById(R.id.resultMessage);
        textView.setText(message);
        textView.setTextColor(Color.BLACK);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}