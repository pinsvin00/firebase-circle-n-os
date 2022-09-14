package com.example.circol;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class OnlineGameHandler {



    ClientConnectionData clientConnectionData;
    OnlineGameData connectionData;
    GameAPI game;
    DatabaseReference gameReference;

    public boolean


    public void handleWin(Mark mark) {

    }

    public void handleClick(int x, int y, Mark mark) {
        this.connectionData.moves.add(new Move(this.clientConnectionData.mark, x, y));
        this.gameReference.child(this.connectionData.uid).setValue(this.connectionData);
    }

    public OnlineGameHandler(ClientConnectionData clientConnectionData, OnlineGameData connectionData) {
        this.clientConnectionData = clientConnectionData;
        this.connectionData = connectionData;
        this.gameReference = FirebaseProvider.get().getReference("games");

        gameReference.child(connectionData.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OnlineGameData conn = dataSnapshot.getValue(OnlineGameData.class);
                Move lastMove = conn.moves.get(conn.moves.size() - 1);
                if (lastMove.mark != clientConnectionData.mark) {
                    game.markMove(lastMove.x, lastMove.y);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
