package ru.kotov.service.impl;

import ru.kotov.dao.AppUserDAO;
import ru.kotov.entity.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.kotov.dao.RawDataDAO;
import ru.kotov.entity.RawData;
import ru.kotov.service.MainService;
import ru.kotov.service.ProducerService;

import static ru.kotov.entity.enums.UserState.BASIC_STATE;

@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    public final ProducerService producerService;
    private final AppUserDAO appUserDAO;
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var appUser = findOrSaveAppUser(telegramUser);
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(message.getText());
        producerService.producerAnswer(sendMessage);
    }
    private AppUser findOrSaveAppUser(User telegramBotUser) {
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramBotUser.getId());
        if(persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramBotUser.getId())
                    .username(telegramBotUser.getUserName())
                    .firstName(telegramBotUser.getFirstName())
                    .lastName(telegramBotUser.getLastName())
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);

        }
        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
