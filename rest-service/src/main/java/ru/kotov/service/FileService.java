package ru.kotov.service;

import org.springframework.core.io.FileSystemResource;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.AppPhoto;
import ru.kotov.entity.BinaryContent;

import java.io.IOException;

public interface FileService {
    AppDocument getDocument(String hash);
    AppPhoto getPhoto(String hash);
}
