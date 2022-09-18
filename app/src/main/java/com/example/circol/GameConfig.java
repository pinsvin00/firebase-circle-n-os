package com.example.circol;

import java.io.Serializable;

public class GameConfig implements Serializable {
    boolean isOnline;
    long botMoveDelay;
    String gameUUID;
    Mark playerMark;

    OnlineGameData onlineGameData;

    public GameConfig(boolean isOnline, long botMoveDelay, String gameUUID) {
        this.isOnline = isOnline;
        this.botMoveDelay = botMoveDelay;
        this.gameUUID = gameUUID;
    }

    public GameConfig(Builder builder) {
        this.isOnline = builder.isOnline;
        this.botMoveDelay = builder.botMoveDelay;
        this.gameUUID = builder.gameUUID;
        this.playerMark = builder.playerMark;
        this.onlineGameData = builder.onlineGameData;
    }


    public static class Builder {
        private final boolean isOnline;
        private long botMoveDelay;
        private String gameUUID;
        private Mark playerMark;
        private OnlineGameData onlineGameData;

        public Builder(boolean isOnline) {
            this.isOnline = isOnline;
        }

        public Builder setBotMoveDelay(long botMoveDelay) {
            this.botMoveDelay = botMoveDelay;
            return this;
        }

        public Builder setGameUUID(String gameUUID) {
            this.gameUUID = gameUUID;
            return this;
        }

        public Builder setPlayerMark(Mark playerMark) {
            this.playerMark = playerMark;
            return this;
        }

        public Builder setOnlineGameData(OnlineGameData onlineGameData) {
            this.onlineGameData = onlineGameData;
            return this;
        }

        public GameConfig build() {
            return new GameConfig(this);
        }

    }

}
