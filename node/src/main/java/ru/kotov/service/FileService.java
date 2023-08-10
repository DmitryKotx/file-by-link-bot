package ru.kotov.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.AppPhoto;
import ru.kotov.service.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generateLink(Long id, LinkType linkType);
}
