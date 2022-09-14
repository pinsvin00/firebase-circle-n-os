package com.example.circol;

public class GameHandler {

    protected Mark[][] board;
    protected Mark currentMark;
    protected BoardAPI boardAPI;
    MainActivity activity;

    public String markToStr(Mark mark) {
        if(mark == Mark.CIRCLE) return "Kółko";
        if(mark == Mark.CROSS) return "Krzyzyk";
        if(mark == Mark.EMPTY) return "Nikt";

        return "";
    }

    public GameHandler() {
        this.board = new Mark[3][3];

        for (int i  = 0 ; i < 3 ; i++) {
            for ( int j = 0 ; j < 3 ; j++) {
                this.board[i][j] = Mark.EMPTY;
            }
        }
    }


    public void handleWin(Mark winningMark) {}

    protected void markMove(int i, int j, Mark mark) {

        if(this.board[i][j] != Mark.EMPTY) return;
        this.board[i][j] = this.currentMark;

        boolean draw = this.checkDraw();
        Mark winner = this.checkWin();


        if (winner != Mark.EMPTY || draw) {
            this.handleWin(this.currentMark);
        }

        //mark field on board with proper mark
        this.boardAPI.markFieldAsMark(i, j , mark);

        currentMark = Mark.CIRCLE == currentMark ? Mark.CROSS : Mark.CIRCLE;

    }

    public void handleFieldClick(int i, int j) {
        this.markMove(i, j, this.currentMark);
    }

    Mark checkWin() {
        for (int i = 0; i < 3; i++) {

            boolean rowWin = true;
            Mark val = this.board[i][0];

            for (int j = 1; j < 3; j++) {
                if (this.board[i][j] != val) {
                    rowWin = false;
                }
            }

            if (rowWin) return val;
        }

        for (int i = 0; i < 3; i++) {

            boolean rowWin = true;
            Mark val = this.board[0][i];

            for (int j = 1; j < 3; j++) {
                if (this.board[j][i] != val) {
                    rowWin = false;
                }
            }

            if (rowWin) return val;
        }

        if (this.board[0][0] == this.board[1][1] && this.board[1][1] == this.board[2][2]) {
            return this.board[0][0];
        }

        if (this.board[2][0] == this.board[1][1] && this.board[0][2] == this.board[1][1]) {
            return this.board[2][0];
        }

        return Mark.EMPTY;


    }

    boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board[i][j] == Mark.EMPTY) return false;
            }
        }

        return true;
    }
}
