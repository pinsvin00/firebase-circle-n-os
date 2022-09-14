package com.example.circol;

import java.sql.Timestamp;
import java.util.UUID;

class ConnectionReadiness {
    public String uid;
    public Timestamp timestamp;

    public ConnectionReadiness() {
        this.uid = UUID.randomUUID().toString();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
