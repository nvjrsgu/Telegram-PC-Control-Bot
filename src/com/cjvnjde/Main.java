package com.cjvnjde;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.io.File;

public class Main{

    public static void main(String[] args) {



        Properties prop = new Properties("./settings");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TelegramPCC(prop.botUserName, prop.botToken, prop.myChatId));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
