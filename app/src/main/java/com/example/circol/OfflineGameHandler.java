package com.example.circol;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.UUID;

public class OfflineGameHandler extends GameHandler {

    public boolean end = false;
    public OfflineGameHandler() {
        super();
        this.getRandomMark();
    }

    public Mark playerMark;

    public void getRandomMark() {
        Random random =new Random();
        if( random.nextInt(10) % 2 == 0) {
            this.playerMark = Mark.CIRCLE;
        }
        else {
            this.playerMark = Mark.CROSS;
        }
    }

    private Mark oppositeMark(Mark mark) {
        if(mark == Mark.CIRCLE) return Mark.CROSS;
        else return Mark.CROSS;
    }

    public void handleWin(Mark mark) {
        System.out.println("epickie wygranko");
        String markStr = markToStr(mark);
        activity.createMessageBox(markStr + " had won!",
                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Rewanż?";
                    }


                    @Override
                    public void handler(AppCompatActivity activity) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                },

                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Wracam do menu!";
                    }

                    @Override
                    public void handler(AppCompatActivity activity) {
                        Intent intent = new Intent(activity, MainMenu.class);
                        activity.startActivity(intent);
                    }
                }
        );
    }

    public void redirectToMainMenu() {
        Intent intent = new Intent(activity, MainMenu.class);
        activity.startActivity(intent);
    }

    public void makeBotMove() {
        Random rand = new Random();

        int x = rand.nextInt(3);
        int y = rand.nextInt(3);

        while(this.board[x][y] != Mark.EMPTY) {
             x = rand.nextInt(3);
             y = rand.nextInt(3);
        }

        markMove(x, y, this.oppositeMark(this.playerMark));

    }


    @Override
    public void handleFieldClick(int x, int y) {

        if(end) return;
        this.markMove(x, y, Mark.CIRCLE);
        this.makeBotMove();

        if(this.checkWin() != Mark.EMPTY) {
            this.end = true;
            handleWin(this.checkWin());
        }
    }



}
