package ru.kotov.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    public TelegramBot(@Value("${bot.token}")String botToken, UpdateController updateController) {
        super(botToken);
        this.updateController = updateController;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }
    private final UpdateController updateController;
    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }
    @Override
    public void onUpdateReceived(Update update) {
       updateController.processUpdate(update);
    }
    public void sendAnswerMessage(SendMessage message) {
        if(message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(String.valueOf(e));
            }
        }
    }

}