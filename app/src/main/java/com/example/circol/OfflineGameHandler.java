package com.example.circol;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OfflineGameHandler extends GameHandler {
    public long botMoveDelay;
    public OfflineGameHandler(Mark myMark) {
        super();
        this.myMark = myMark;

    }

    private class BotMoveTask extends TimerTask{

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeBotMove();
                    currentMark = myMark;
                }
            });
        }
    }


    public void scheduleBotMove() {
        Timer timer = new Timer();
        timer.schedule(new BotMoveTask(), this.botMoveDelay);
    }


    public void handleWin(Mark mark) {
        String winStr = getWinStr(mark);
        activity.createMessageBox(winStr,
                new EndGameInterface() {
                    @Override
                    public String message() {
                        return "Rewanż?";
                    }


                    @Override
                    public void handler(AppCompatActivity activity) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra("config", new GameConfig.Builder(false).
                                setBotMoveDelay(1000L).setPlayerMark(oppositeMark(myMark)).build()
                        );

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
                        redirectToMainMenu();
                    }
                }
        );
    }

    public void redirectToMainMenu() {
        Intent intent = new Intent(activity, MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        markMove(x, y, myMark == Mark.CIRCLE ? Mark.CROSS : Mark.CIRCLE);
        currentMark = myMark;

    }

    @Override
    public void handleFieldClick(int x, int y) {
        if(end) return;
        if(this.currentMark != this.myMark) return;
        this.currentMark = oppositeMark(this.myMark);

        this.markMove(x, y, myMark);

        this.scheduleBotMove();

    }
}

