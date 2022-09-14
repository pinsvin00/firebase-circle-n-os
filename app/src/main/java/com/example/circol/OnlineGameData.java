package com.example.circol;

import java.io.Serializable;
import java.util.ArrayList;

class Move implements Serializable {
    public Mark mark;
    int x;
    int y;

    public Move(Mark mark, int x, int y) {
        this.mark = mark;
        this.x = x;
        this.y = y;
    }
}

class OnlineGameData implements Serializable {
    public String uid;
    public ArrayList<Move> moves = new ArrayList<Move>();


}
