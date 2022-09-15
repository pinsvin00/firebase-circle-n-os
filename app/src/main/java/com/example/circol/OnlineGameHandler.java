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



    public void handleWin(Mark mark) {

        String markStr = markToStr(mark);
        activity.createMessageBox(markStr + " had won!",
                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Rewanż?";
                    }


                    @Override
                    public void handler(AppCompatActivity activity) {
                        if(clientData.mark == Mark.CIRCLE) {
                            gameData.circleState = PlayerState.REVENGE;
                        }
                        else {
                            gameData.crossState = PlayerState.REVENGE;
                        }

                        gameData.revengeLobbyUUID = UUID.randomUUID().toString();
                        updateGameData();

                    }
                },

                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Wracam do menu!";
                    }

                    @Override
                    public void handler(AppCompatActivity activity) {
                        if(clientData.mark == Mark.CIRCLE) {
                            gameData.circleState = PlayerState.LEFT;
                        }
                        else {
                            gameData.crossState = PlayerState.LEFT;
                        }

                        updateGameData();

                        Intent intent = new Intent(activity, MainMenu.class);
                        activity.startActivity(intent);
                    }
                }
        );
    }

    public void redirectToMainMenu() {
        Intent intent = new Intent(activity, ConnectionScreen.class);
        activity.startActivity(intent);
    }



    public void updateGameData() {
        this.gameReference.setValue(this.gameData);
    }

    @Override
    public void handleFieldClick(int x, int y) {

        if(!clientData.mark.equals(gameData.move)) return;

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

                if(gameData.crossState == PlayerState.LEFT || gameData.circleState == PlayerState.LEFT) {
                    redirectToMainMenu();
                }

                if(gameData.circleState == PlayerState.REVENGE && gameData.crossState == PlayerState.REVENGE) {
                    Intent intent = new Intent(activity, MainActivity.class);

                    OnlineGameData onlineGameData = new OnlineGameData();
                    onlineGameData.uid = gameData.revengeLobbyUUID;

                    intent.putExtra("client-data", clientData);
                    intent.putExtra("conn-data", onlineGameData);
                    intent.putExtra("isOnline", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    OnlineGameData newData = new OnlineGameData();
                    newData.uid = gameData.revengeLobbyUUID;
                    newData.move = Mark.CIRCLE;
                    DatabaseReference gameRef =  FirebaseProvider.get().getReference("tictactoe").child("games").child(newData.uid);
                    gameRef.setValue(newData);

                    gameReference.removeEventListener(this);
                    activity.startActivity(intent);
                }

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

                updateGameData();

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
