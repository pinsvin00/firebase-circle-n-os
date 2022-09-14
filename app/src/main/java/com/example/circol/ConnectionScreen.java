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


public class ConnectionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FirebaseDatabase db = FirebaseProvider.get();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_screen);




        ConnectionReadiness conn = new ConnectionReadiness();
        db.getReference("conns").child(conn.uid).setValue(conn);


        DatabaseReference gameReference = db.getReference("games");

        gameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //apparanetly a new player has appeared!
                OnlineGameData data = dataSnapshot.getValue(OnlineGameData.class);
                if(data.uid.equals(conn.uid)) {

                    Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);

                    OnlineGameData connectionData = dataSnapshot.getValue(OnlineGameData.class);

                    ClientConnectionData clientData = new ClientConnectionData();
                    clientData.mark = Mark.CIRCLE;

                    intent.putExtra("client-data", clientData);
                    intent.putExtra("conn-data", connectionData);

                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //jesli pojawi sie nowe polaczenie
        db.getReference("conns").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ConnectionReadiness post = dataSnapshot.getValue(ConnectionReadiness.class);
                Intent intent = new Intent(ConnectionScreen.this, MainActivity.class);

                OnlineGameData connectionData = new OnlineGameData();
                gameReference.child(post.uid).setValue(connectionData);


                ClientConnectionData clientData = new ClientConnectionData();
                clientData.mark = Mark.CROSS;

                intent.putExtra("conn-data", connectionData);
                intent.putExtra("client-data", clientData);

                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });





//        ConnectionHandler handler = new ConnectionHandler();
//        handler.run();

    }
}