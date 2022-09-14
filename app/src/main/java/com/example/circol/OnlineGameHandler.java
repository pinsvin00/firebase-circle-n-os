package com.example.circol;

import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class OnlineGameHandler extends GameHandler {



    ClientConnectionData clientData;
    OnlineGameData gameData;
    DatabaseReference gameReference;
    DatabaseReference movesReference;


    boolean wantToRevenge = false;


    public void handleWin(Mark mark) {

        String markStr = markToStr(mark);
        activity.createMessageBox(markStr + "had won",
                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Rewanż?";
                    }

                    @Override
                    public void handler(AppCompatActivity activity) {
                        Intent intent = new Intent(activity, ConnectionScreen.class);
                        activity.startActivity(intent);
                    }
                },

                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Wracam do menu!";
                    }

                    @Override
                    public void handler(AppCompatActivity activity) {
                        Intent intent = new Intent(activity, MainMenu.class);
                        activity.startActivity(intent);
                    }
                }
        );


    }


    public void updateGameData() {
        this.gameReference.setValue(this.gameData);
    }

    @Override
    public void handleFieldClick(int x, int y) {


        System.out.println(markToStr(clientData.mark));

        if(clientData.mark != this.currentMark) return;


        String moveUUID = UUID.randomUUID().toString();
        this.movesReference.child(moveUUID).setValue(new Move(clientData.mark, x, y));
    }

    public OnlineGameHandler(ClientConnectionData clientConnectionData, OnlineGameData connectionData) {

        super();

        this.clientData = clientConnectionData;
        this.gameData = connectionData;
        this.gameReference = FirebaseProvider.get().getReference("tictactoe").child("games").child(connectionData.uid);
        this.movesReference = this.gameReference.child("moves");
        this.currentMark = this.gameData.move;


        gameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Update the game data stored in app
                OnlineGameHandler.this.gameData = dataSnapshot.getValue(OnlineGameData.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        this.movesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Move lastMove =  dataSnapshot.getValue(Move.class);

                OnlineGameHandler.this.markMove(lastMove.x, lastMove.y, lastMove.mark);

                gameData.move = Mark.CIRCLE == lastMove.mark ? Mark.CROSS : Mark.CIRCLE;
                OnlineGameHandler.this.currentMark = gameData.move;

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }
}
