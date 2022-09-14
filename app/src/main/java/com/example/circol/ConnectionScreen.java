package com.example.circol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;


public class ConnectionScreen extends AppCompatActivity {
    ConnectionReadiness conn;

    FirebaseDatabase db = FirebaseProvider.get();
    DatabaseReference ticTacToe = db.getReference("tictactoe");
    DatabaseReference connections = ticTacToe.child("connections");
    DatabaseReference games = ticTacToe.child("games");


    ChildEventListener connectionListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ConnectionReadiness partnerReadiness = dataSnapshot.getValue(ConnectionReadiness.class);
            if( partnerReadiness.milis.compareTo(conn.milis) > 0 ) {
                System.out.println(conn + "|" + partnerReadiness);

                OnlineGameData onlineGameData = new OnlineGameData();
                onlineGameData.uid = conn.uuid;
                onlineGameData.move = Mark.CIRCLE;

                games.child(onlineGameData.uid).setValue(onlineGameData);

                ClientConnectionData clientData = new ClientConnectionData();
                clientData.mark = Mark.CROSS;

                Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);
                intent.putExtra("conn-data", onlineGameData);
                intent.putExtra("client-data", clientData);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                connections.child(partnerReadiness.uuid).removeValue();
                connections.child(conn.uuid).removeValue();

                startActivity(intent);
            }
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

    ChildEventListener joinToGame = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            OnlineGameData data = dataSnapshot.getValue(OnlineGameData.class);
            if( data.milis.compareTo(conn.milis) > 0 && !data.uid.equals(conn.uuid) ) {
                Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);
                ClientConnectionData clientData = new ClientConnectionData();
                clientData.mark = Mark.CIRCLE;

                intent.putExtra("client-data", clientData);
                intent.putExtra("conn-data", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            }
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


    public void onDestroy() {
        // Must always call the super method at the end.
        System.out.println("Destroyed :( ");
        connections.removeEventListener(this.connectionListener);
        games.removeEventListener(this.joinToGame);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_screen);

        conn = new ConnectionReadiness();
        conn.uuid = UUID.randomUUID().toString();
        conn.milis = System.currentTimeMillis();


        System.out.println("MY ID IS " + conn.uuid);

        connections.child(conn.uuid).setValue(conn);

        games.addChildEventListener(this.joinToGame);
        connections.addChildEventListener(this.connectionListener);


    }
}