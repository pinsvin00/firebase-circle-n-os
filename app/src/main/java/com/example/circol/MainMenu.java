package com.example.circol;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainMenu extends AppCompatActivity {


    private void redirectToConnectionScreen(Mark mark) {
        Intent intent = new Intent(MainMenu.this, ConnectionScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("mark", mark);
        startActivity(intent);
    }

    private void redirectToOfflinePlay(Mark mark) {
        Intent intent = new Intent(MainMenu.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra("config", new GameConfig.Builder(false).
                setPlayerMark(mark).
                setBotMoveDelay(1000l).
                build()
        );

        startActivity(intent);
    }

    private void handlePlay(boolean isOnline) {
        AlertDialog dialog = new AlertDialog.Builder(this).
                setTitle("Wybierz swój znaczek").
                setCancelable(false).
                setNegativeButton("KÓŁKO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(isOnline) {
                            redirectToConnectionScreen(Mark.CIRCLE);
                        } else {
                            redirectToOfflinePlay(Mark.CIRCLE);
                        }

                    }
                }).
                setPositiveButton("KRZYŻYK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(isOnline) {
                            redirectToConnectionScreen(Mark.CROSS);
                        } else {
                            redirectToOfflinePlay(Mark.CROSS);
                        }
                    }

                }).
                create();

        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        System.out.println("Initialized main menu!");

        findViewById(R.id.gaming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlay(true);
            }
        });

        findViewById(R.id.gamingSolo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePlay(false);
            }
        });
    }
}