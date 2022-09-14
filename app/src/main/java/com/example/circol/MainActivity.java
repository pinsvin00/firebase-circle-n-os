package com.example.circol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


enum Mark {
    EMPTY,
    CIRCLE,
    CROSS
}


class BoardGenerator {

    private List<Button> buttons;
    private Context ctx;
    private Button notifier;

    public BoardGenerator(Context ctx, Button notifier) {
        this.ctx = ctx;
        this.notifier = notifier;
    }



}


public class MainActivity extends AppCompatActivity {


    public void goToEndScreen(String Message) {
        Intent intent =  new Intent(this, EndScreen.class);
        intent.putExtra("message", Message);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean isOnlineGame = true ; //this.getIntent()
        ClientConnectionData clientConnectionData = (ClientConnectionData) this.getIntent().getSerializableExtra("client-data");
        OnlineGameData connectionData = (OnlineGameData) this.getIntent().getSerializableExtra("conn-data");
        OnlineGameHandler handler = new OnlineGameHandler(clientConnectionData, connectionData);


        Context ctx = this.getBaseContext();
        GameAPI game = new GameAPI(handler);
        game.lol = this;


        Button notifier = findViewById(R.id.move_notify_text);
        BoardGenerator generator = new BoardGenerator(ctx, notifier);

        List<FlexboxLayout> layouts = new ArrayList<>();





        layouts.add( findViewById(R.id.row0) );
        layouts.add( findViewById(R.id.row1) );
        layouts.add( findViewById(R.id.row2) );
        game.rows = layouts;


    }
}
