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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;


public class ConnectionScreen extends AppCompatActivity {


    void logga(String a) {
        System.out.println("ID " +  conn.uuid +  " " + a);
    }


    ConnectionReadiness conn;
    boolean firebaza = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FirebaseDatabase db = FirebaseProvider.get();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_screen);

        conn = new ConnectionReadiness();
        conn.uuid = UUID.randomUUID().toString();
        conn.milis = System.currentTimeMillis();


        System.out.println("MY ID IS " + conn.uuid);

        DatabaseReference ticTacToe = db.getReference("tictactoe");
        DatabaseReference connections = ticTacToe.child("connections");
        DatabaseReference games = ticTacToe.child("games");

        connections.child(conn.uuid).setValue(conn);

        games.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                OnlineGameData data = dataSnapshot.getValue(OnlineGameData.class);
                if( data.milis.compareTo(conn.milis) > 0 && !data.uid.equals(conn.uuid) ) {
                    Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);
                    ClientConnectionData clientData = new ClientConnectionData();
                    clientData.mark = Mark.CIRCLE;

                    intent.putExtra("client-data", clientData);
                    intent.putExtra("conn-data", data);

                    startActivity(intent);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });


        connections.addChildEventListener(new ChildEventListener() {
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

                    connections.child(partnerReadiness.uuid).removeValue();
                    connections.child(conn.uuid).removeValue();

                    startActivity(intent);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





//        ConnectionHandler handler = new ConnectionHandler();
//        handler.run();

    }
}