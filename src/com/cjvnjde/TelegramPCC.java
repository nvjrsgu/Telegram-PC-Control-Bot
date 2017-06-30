package com.cjvnjde;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nvjrsgu on 6/29/2017.
 */

// 248289485
public class TelegramPCC extends TelegramLongPollingBot {

    private String botUsername, botToken, myChatId;
    private KeyboardRow keyboardFirstRow = new KeyboardRow();
    private KeyboardRow keyboardSecondRow = new KeyboardRow();

    TelegramPCC(String botUsername, String botToken, String myChatId){
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.myChatId = myChatId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText() && message.getChatId().toString().equals(myChatId)){
            String messageText = message.getText();
            String chatId = message.getChatId().toString();
            keyboardFirstRow.clear();
            keyboardSecondRow.clear();
            switch (messageText){
                case "No":
                case "Back":
                case "/start":
                    //sendMsg("Hello!",chatId);
                    keyboardFirstRow.add("PrintScreen");
                    keyboardSecondRow.add("Shutdown");
                    menu(keyboardFirstRow, keyboardSecondRow, "What do you want?", chatId);
                    break;
                case "/print_screen":
                case "PrintScreen":
                    keyboardFirstRow.add("File");
                    keyboardFirstRow.add("Photo");
                    keyboardSecondRow.add("Back");
                    menu(keyboardFirstRow, keyboardSecondRow, "Choose format", chatId);
                    break;
                case  "File":
                    sendPhotoDocument(ComputerController.createScreenCapture(), chatId);
                    keyboardFirstRow.add("PrintScreen");
                    keyboardSecondRow.add("Shutdown");
                    menu(keyboardFirstRow, keyboardSecondRow, "Your photo", chatId);
                    break;
                case "Photo":
                    sendPhoto(ComputerController.createScreenCapture(), chatId);
                    keyboardFirstRow.add("PrintScreen");
                    keyboardSecondRow.add("Shutdown");
                    menu(keyboardFirstRow, keyboardSecondRow, "Your photo", chatId);
                    break;
                case "/shutdown":
                case "Shutdown":
                    keyboardFirstRow.add("Yes");
                    keyboardFirstRow.add("No");
                    keyboardSecondRow.add("Back");
                    menu(keyboardFirstRow, keyboardSecondRow, "Are you sure?", chatId);
                    break;
                case "Yes":
                    keyboardFirstRow.add("PrintScreen");
                    keyboardSecondRow.add("Shutdown");
                    menu(keyboardFirstRow, keyboardSecondRow, "", chatId);
                    ComputerController.shutdownComputer();
                    break;
                default:
                    keyboardFirstRow.add("PrintScreen");
                    keyboardSecondRow.add("Shutdown");
                    menu(keyboardFirstRow, keyboardSecondRow, "", chatId);
                    break;
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendPhotoDocument(BufferedImage im, String chatId){
        SendDocument document = new SendDocument();
        Date d = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(im,"jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = new ByteArrayInputStream(os.toByteArray());

        document.setNewDocument(dataFormat.format(d)+".jpg", is);
        document.setChatId(chatId);
        try {
            sendDocument(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(BufferedImage im, String chatId){
        SendPhoto photo = new SendPhoto();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(im, "jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = new ByteArrayInputStream(os.toByteArray());

        photo.setNewPhoto("name.jpg", is);
        photo.setChatId(chatId);

        try {
            sendPhoto(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String text, String chatId) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);

        message.setChatId(chatId);
        message.setText(text);

        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void menu(KeyboardRow keyboardFirstRow, KeyboardRow keyboardSecondRow, String text, String chatId){

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        // KeyboardRow keyboardFirstRow = new KeyboardRow();
        // KeyboardRow keyboardSecondRow = new KeyboardRow();

        //  keyboardFirstRow.add("PrintScreen");

        //keyboardSecondRow.add("OffBot");
        // keyboardSecondRow.add("Shutdown PC");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(chatId);
        if(text != null && !text.equals("")) sendMessage.setText(text);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
