package com.example.circol;

public class GameHandler {

    protected Mark[][] board;
    protected Mark currentMark = Mark.CIRCLE;
    protected BoardAPI boardAPI;
    boolean end = false;
    MainActivity activity;
    Mark myMark;

    public String getWinStr(Mark mark) {
        if(mark == Mark.CIRCLE) return "Wygrało Kółko!";
        if(mark == Mark.CROSS) return "Wygrał krzyzyk!";
        if(mark == Mark.EMPTY) return "Remis!";

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

    public Mark oppositeMark(Mark mark) {
        if(mark == Mark.CIRCLE) return Mark.CROSS;
        else return Mark.CIRCLE;
    }


    public void handleWin(Mark winningMark) {}

    protected void markMove(int i, int j, Mark mark) {

        if(this.board[i][j] != Mark.EMPTY || this.end) return;
        this.board[i][j] = mark;
        this.boardAPI.markFieldAsMark(i, j , mark);

        Mark winner = checkGameStatus();
        if(winner != null ) {
            this.end = true;
            handleWin(this.checkGameStatus());
        }



    }

    public void handleFieldClick(int i, int j) {
        this.markMove(i, j, this.currentMark);
    }

    Mark checkGameStatus() {

        for (int i = 0; i < 3; i++) {
            boolean rowWin = true;
            Mark val = this.board[i][0];

            for (int j = 1; j < 3; j++) {
                if (this.board[i][j] != val || this.board[i][j] == Mark.EMPTY) {
                    rowWin = false;
                    break;
                }
            }

            if (rowWin) return val;
        }

        for (int i = 0; i < 3; i++) {

            boolean rowWin = true;
            Mark val = this.board[0][i];
            if(val == Mark.EMPTY) continue;

            for (int j = 1; j < 3; j++) {
                if (this.board[j][i] != val || this.board[i][j] == Mark.EMPTY) {
                    rowWin = false;
                    break;
                }
            }

            if (rowWin) return val;
        }

        if (this.board[0][0] == this.board[1][1] && this.board[1][1] == this.board[2][2] && this.board[0][0] != Mark.EMPTY) {
            return this.board[0][0];
        }

        if (this.board[2][0] == this.board[1][1] && this.board[0][2] == this.board[1][1] && this.board[2][0] != Mark.EMPTY) {
            return this.board[2][0];
        }

        if(checkDraw()) {
            return Mark.EMPTY;
        }

        return null;
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
