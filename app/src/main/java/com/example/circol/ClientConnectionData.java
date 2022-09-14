package com.example.circol;

import java.io.Serializable;

class ClientConnectionData implements Serializable {
    public Mark mark;

    @Override
    public String toString() {
        return "ClientConnectionData{" +
                "mark=" + mark +
                '}';
    }
}
