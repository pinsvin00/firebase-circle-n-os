package com.example.circol;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

class   ConnectionReadiness implements Serializable {
    public String uuid;
    public Long milis;

    @Override
    public String toString() {
        return "ConnectionReadiness{" +
                "uuid='" + uuid + '\'' +
                ", milis=" + milis +
                '}';
    }

    public ConnectionReadiness() {
    }

}
