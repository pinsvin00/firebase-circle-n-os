package com.example.circol;

import java.io.Serializable;

class Move implements Serializable {
    public Mark mark;
    public int x;
    public int y;



    @Override
    public String toString() {
        return "Move{" +
                "mark=" + mark +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public Move(Mark mark, int x, int y) {
        this.mark = mark;
        this.x = x;
        this.y = y;
    }

    public Move() { }
}

enum PlayerState {
    LEFT,
    REVENGE,
    PLAY,
    EMPTY,
}

class OnlineGameData implements Serializable {
    public String uid;
    public Long millis = System.currentTimeMillis();
    public PlayerState circleState = PlayerState.EMPTY;
    public PlayerState crossState =  PlayerState.EMPTY;

    //revenge related
    public String revengeLobbyUUID = "";


    @Override
    public String toString() {
        return "OnlineGameData{" +
                "uid='" + uid + '\'' +
                ", milis=" + millis +
                ", circleState=" + circleState +
                ", crossState=" + crossState +
                ", revengeLobbyUUID='" + revengeLobbyUUID + '\'' +
                ", move=" + move +
                '}';
    }

    public Mark move;
}
