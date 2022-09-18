package com.example.circol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ConnectionScreen extends AppCompatActivity {
    ConnectionReadiness playerData;
    OnlineGameData gameData;

    boolean foundGameToConnect = false;
    boolean initialGamesFetch = false;
    boolean joinedToMyGame = false;

    FirebaseDatabase db = FirebaseProvider.get();
    DatabaseReference ticTacToe = db.getReference("tictactoe");
    DatabaseReference games = ticTacToe.child("games");

    TimerTask playSinglePlayer;

    Mark selectedMark;

    ValueEventListener checkForGamesToJoin = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(initialGamesFetch) return;

            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                OnlineGameData data = snap.getValue(OnlineGameData.class);

                if(data.uid.equals(gameData.uid)) continue;
                if(data.crossState == PlayerState.LEFT || data.circleState == PlayerState.LEFT) continue;


                boolean connected = false;
                if(data.crossState == PlayerState.EMPTY && selectedMark == Mark.CROSS) {
                    data.crossState = PlayerState.PLAY;
                    connected = true;
                }
                else if(data.circleState == PlayerState.EMPTY && selectedMark == Mark.CIRCLE) {
                    data.circleState = PlayerState.PLAY;
                    connected = true;
                }

                if(connected)  {
                    games.child(data.uid).setValue(data);

                    Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);

                    deleteMyGame();

                    GameConfig config = new GameConfig.Builder(true)
                            .setOnlineGameData(data)
                            .setPlayerMark(selectedMark).build();

                    intent.putExtra("config", config);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    initialGamesFetch = true;

                    return;
                }
            }

            initialGamesFetch = true;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    ValueEventListener checkForMyGameJoined = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



            OnlineGameData data = dataSnapshot.getValue(OnlineGameData.class);

            if(data == null) return;
            if(data.circleState == PlayerState.PLAY && data.crossState == PlayerState.PLAY) {
                foundGameToConnect = true;
                Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);

                joinedToMyGame = true;

                GameConfig config = new GameConfig.Builder(true)
                                .setOnlineGameData(data)
                                .setPlayerMark(selectedMark).build();

                intent.putExtra("config", config);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    void deleteMyGame() {
        games.child(gameData.uid).removeValue();
    }

    public void onDestroy() {
        System.out.println("Destroyed :( ");
        super.onDestroy();

        playSinglePlayer.cancel();

        if(!joinedToMyGame) {
            deleteMyGame();
        }

        games.removeEventListener(checkForGamesToJoin);
        games.child(gameData.uid).removeEventListener(this.checkForMyGameJoined);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_screen);


        this.selectedMark = (Mark) this.getIntent().getSerializableExtra("mark");


        gameData = new OnlineGameData();
        gameData.uid = UUID.randomUUID().toString();
        if(this.selectedMark == Mark.CIRCLE) {
            gameData.circleState = PlayerState.PLAY;
        }
        else {
            gameData.crossState = PlayerState.PLAY;
        }

        gameData.move = Mark.CIRCLE;
        games.child(gameData.uid).setValue(gameData);

         playSinglePlayer = new TimerTask() {
            public void run() {
                Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);

                GameConfig config = new GameConfig.Builder(false)
                        .setBotMoveDelay(1000L)
                        .build();

                intent.putExtra("config", config);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        };


        Timer timer = new Timer("Timer");
        long delay = 10000L;
        timer.schedule(playSinglePlayer, delay);



        playerData = new ConnectionReadiness();
        playerData.uuid = UUID.randomUUID().toString();
        playerData.milis = System.currentTimeMillis();

        games.addValueEventListener(checkForGamesToJoin);
        games.child(gameData.uid).addValueEventListener(this.checkForMyGameJoined);

    }
}