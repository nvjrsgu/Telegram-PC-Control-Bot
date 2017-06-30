package com.cjvnjde;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.imageio.ImageIO;

public class Main{

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TelegramPCC("botUsername", "botToken", "yourChatId"));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
