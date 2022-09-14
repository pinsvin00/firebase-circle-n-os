package com.example.circol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OnlineGameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);


        Intent intent = getIntent();
        String message = intent.getStringExtra("message");


        TextView btn = findViewById(R.id.win_notification);
        btn.setText(message);


        findViewById(R.id.menu_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnlineGameEnd.this, MainMenu.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnlineGameEnd.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}