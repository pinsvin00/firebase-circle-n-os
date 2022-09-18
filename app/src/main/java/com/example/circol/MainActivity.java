package com.example.circol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;


enum Mark {
    EMPTY,
    CIRCLE,
    CROSS
}



public class MainActivity extends AppCompatActivity {

    Button notifier;

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
        this.notifier = findViewById(R.id.move_notify_text);

        GameConfig config = (GameConfig) this.getIntent().getSerializableExtra("config");
        GameHandler handler;

        if(config.isOnline) {
            handler = new OnlineGameHandler(config.playerMark, config.onlineGameData);
        }
        else {
            handler = new OfflineGameHandler(config.playerMark);
        }

        BoardAPI boardAPI = new BoardAPI();

        boardAPI.activity = this;
        boardAPI.ctx = getBaseContext();
        boardAPI.notifier = notifier;


        List<FlexboxLayout> layouts = new ArrayList<>();
        layouts.add( findViewById(R.id.row0) );
        layouts.add( findViewById(R.id.row1) );
        layouts.add( findViewById(R.id.row2) );
        boardAPI.rows = layouts;

        boardAPI.generate();

        handler.activity = this;
        handler.boardAPI = boardAPI;
        boardAPI.handler = handler;



        if(!config.isOnline && config.playerMark == Mark.CROSS) {
            OfflineGameHandler hdlr = (OfflineGameHandler) handler;
            hdlr.botMoveDelay = config.botMoveDelay;
            hdlr.scheduleBotMove();
        }


    }
}
