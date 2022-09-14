package com.example.circol;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;
import java.util.function.Consumer;


class GameAPI {

    public MainActivity lol;

    public Mark[][] board;
    private OnlineGameHandler handler;
    private Mark currentMark;
    private TextView notifier;
    private Context ctx;

    public List<FlexboxLayout> rows;

    public GameAPI(OnlineGameHandler handler) {
        this.handler = handler;
        this.board = new Mark[3][3];

        for (int i  = 0 ; i < 3 ; i++) {
            for ( int j = 0 ; j < 3 ; j++) {
                this.board[i][j] = Mark.EMPTY;
            }
        }
    }

    private Button getButton(int x, int y) {
        return (Button) this.rows.get(x).getChildAt(y);
    }

    public void generate() {
        for(int i =  0; i < rows.size(); i++) {
            for (int j = 0 ; j < 3 ; j++) {
                Button b = new Button(this.ctx);
                b.setId( View.generateViewId() );
                b.setBackgroundResource(R.drawable.empty);

                int finalI = i;
                int finalJ = j;

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GameAPI.this.markMove(finalI, finalJ);
                    }
                });
                this.rows.get(i).addView(b);
            }
        }
    }


    public boolean markMove(int i, int j) {

        if(this.board[i][j] != Mark.EMPTY) return false;

        Button clickedButton = this.getButton(i, j);
        if(GameAPI.this.currentMark == Mark.CIRCLE) {
            notifier.setText("Ruch krzyzka");
            clickedButton.setBackgroundResource(R.drawable.o);
        }
        else {
            notifier.setText("Ruch kolka");
            clickedButton.setBackgroundResource(R.drawable.x);
        }

        this.board[i][j] = this.currentMark;

        boolean draw = this.checkDraw();
        Mark winner = this.checkWin();
        if (winner != Mark.EMPTY || draw) {
            handler.handleWin(this.currentMark);
        }

        GameAPI.this.handler.handleClick(i, j, Mark.CROSS);

        return true;
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
