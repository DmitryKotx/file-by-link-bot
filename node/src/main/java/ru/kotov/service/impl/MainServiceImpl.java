package ru.kotov.service.impl;

import lombok.extern.log4j.Log4j;
import ru.kotov.dao.AppUserDAO;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.AppPhoto;
import ru.kotov.entity.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.kotov.dao.RawDataDAO;
import ru.kotov.entity.RawData;
import ru.kotov.exceptions.UploadFileException;
import ru.kotov.service.FileService;
import ru.kotov.service.MainService;
import ru.kotov.service.ProducerService;
import ru.kotov.service.enums.ServiceCommand;

import static ru.kotov.entity.enums.UserState.BASIC_STATE;
import static ru.kotov.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static ru.kotov.service.enums.ServiceCommand.*;

@Log4j
@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    public final ProducerService producerService;
    private final AppUserDAO appUserDAO;
    private final FileService fileService;
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        var serviceCommand = ServiceCommand.fromValue(text);
        if(CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if(BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if(WAIT_FOR_EMAIL_STATE.equals(userState)) {

        }else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова.";
        }
        var chatId = update.getMessage().getChatId();
        setAnswer(output, chatId);

    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            var answer = "Документ успешно загружен! Ссылка для скачивания: *ссылка*";
            setAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            setAnswer(error, chatId);
        }

    }


    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            var answer = "Фото успешно загружено! Ссылка для скачивания: *ссылка*";
            setAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            setAnswer(error, chatId);
        }
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if(!appUser.getIsActive()) {
            var error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
            setAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
            setAnswer(error, chatId);
            return true;
        }
        return false;
    }

    private void setAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String text) {
        var serviceCommand = ServiceCommand.fromValue(text);
        if(REGISTRATION.equals(serviceCommand)) {
            return "Временно недоступно";
        } else if(HELP.equals(serviceCommand)) {
            return help();
        } else if(START.equals(serviceCommand)) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }
    }
    private String help() {
        return """
                Список доступных команд:
                /cancel - отмена выполнения текущей команды;
                /registration - регистрация пользователя.""";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";
    }

    private AppUser findOrSaveAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
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
