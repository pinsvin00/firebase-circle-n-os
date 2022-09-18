package com.example.circol;

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

    OnlineGameData gameData;
    DatabaseReference gameReference;
    DatabaseReference movesReference;

    void removeListeners() {
        gameReference.removeEventListener(this.gameChangedListener);
        movesReference.removeEventListener(this.newMoveListener);
    }


    ChildEventListener newMoveListener = new ChildEventListener() {
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
    };

    ValueEventListener gameChangedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            System.out.println(gameData);
            OnlineGameHandler.this.gameData = dataSnapshot.getValue(OnlineGameData.class);

            if(gameData.crossState == PlayerState.LEFT || gameData.circleState == PlayerState.LEFT) {
                redirectToMainMenu();
            }
            if(gameData.circleState == PlayerState.REVENGE && gameData.crossState == PlayerState.REVENGE) {
                redirectToRevengeGame();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void redirectToRevengeGame() {
        Intent intent = new Intent(activity, MainActivity.class);

        OnlineGameData onlineGameData = new OnlineGameData();
        onlineGameData.uid = gameData.revengeLobbyUUID;

        GameConfig config = new GameConfig.Builder(true).
                setPlayerMark(oppositeMark(myMark)).
                setOnlineGameData(onlineGameData).build();

        intent.putExtra("config", config);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        OnlineGameData newData = new OnlineGameData();
        newData.uid = gameData.revengeLobbyUUID;
        newData.move = Mark.CIRCLE;
        DatabaseReference gameRef =  FirebaseProvider.get().getReference("tictactoe").child("games").child(newData.uid);
        gameRef.setValue(newData);

        removeListeners();

        activity.startActivity(intent);
    }

    public void handleWin(Mark mark) {

        String winStr = getWinStr(mark);
        activity.createMessageBox(winStr,
                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Rewanż?";
                    }
                    @Override
                    public void handler(AppCompatActivity activity) {
                        if(myMark == Mark.CIRCLE) {
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
                        if(myMark == Mark.CIRCLE) {
                            gameData.circleState = PlayerState.LEFT;
                        }
                        else {
                            gameData.crossState = PlayerState.LEFT;
                        }

                        updateGameData();
                        removeListeners();

                        Intent intent = new Intent(activity, MainMenu.class);
                        activity.startActivity(intent);
                    }
                }
        );
    }

    public void redirectToMainMenu() {
        Intent intent = new Intent(activity, MainMenu.class);
        activity.startActivity(intent);
    }



    public void updateGameData() {
        this.gameReference.setValue(this.gameData);
    }

    @Override
    public void handleFieldClick(int x, int y) {
        System.out.println(this.gameData.uid);

        if(!myMark.equals(gameData.move)) return;

        String moveUUID = UUID.randomUUID().toString();
        this.movesReference.child(moveUUID).setValue(new Move(myMark, x, y));
    }

    public OnlineGameHandler(Mark playerMark, OnlineGameData connectionData) {

        super();

        this.myMark = playerMark;
        this.gameData = connectionData;
        this.gameReference = FirebaseProvider.get().getReference("tictactoe").child("games").child(connectionData.uid);
        this.movesReference = this.gameReference.child("moves");
        this.currentMark = this.gameData.move;


        gameReference.addValueEventListener(this.gameChangedListener);
        movesReference.addChildEventListener(this.newMoveListener);


    }
}
