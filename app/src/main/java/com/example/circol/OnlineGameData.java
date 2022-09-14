package com.example.circol;

import java.io.Serializable;
import java.util.ArrayList;

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

class OnlineGameData implements Serializable {
    public String uid;
    public Long milis = System.currentTimeMillis();
    public Mark move;
}
