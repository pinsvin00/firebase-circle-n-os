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
}

class OnlineGameData implements Serializable {
    public String uid;
    public Long milis = System.currentTimeMillis();
    public PlayerState circleState = PlayerState.PLAY;
    public PlayerState crossState =  PlayerState.PLAY;

    //revenge related
    public String revengeLobbyUUID = "";




    public Mark move;
}
