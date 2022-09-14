package com.example.circol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


enum Mark {
    EMPTY,
    CIRCLE,
    CROSS
}



public class MainActivity extends AppCompatActivity {

    Button notifier;

    public void goToEndScreen(String Message) {
        Intent intent =  new Intent(this, EndScreen.class);
        intent.putExtra("message", Message);
        startActivity(intent);
    }

    public void createMessageBox(
            String message,
            EndGameInterface e1,
            EndGameInterface e2
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(e1.message(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        e1.handler(MainActivity.this);
                    }
                })
                .setNegativeButton(e2.message(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        e2.handler(MainActivity.this);
                    }
                });

        builder.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean isOnlineGame = true ; //this.getIntent()

        this.notifier = findViewById(R.id.move_notify_text);

        ClientConnectionData clientConnectionData = (ClientConnectionData) this.getIntent().getSerializableExtra("client-data");
        OnlineGameData connectionData = (OnlineGameData) this.getIntent().getSerializableExtra("conn-data");

        OnlineGameHandler handler = new OnlineGameHandler(clientConnectionData, connectionData);
        handler.activity = this;

        BoardAPI boardAPI = new BoardAPI();

        handler.boardAPI = boardAPI;
        boardAPI.activity = this;
        boardAPI.ctx = getBaseContext();
        boardAPI.handler = handler;
        boardAPI.notifier = notifier;

        List<FlexboxLayout> layouts = new ArrayList<>();
        layouts.add( findViewById(R.id.row0) );
        layouts.add( findViewById(R.id.row1) );
        layouts.add( findViewById(R.id.row2) );
        boardAPI.rows = layouts;

        boardAPI.generate();








    }
}
