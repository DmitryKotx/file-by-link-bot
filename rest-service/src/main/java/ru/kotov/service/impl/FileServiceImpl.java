package ru.kotov.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.kotov.dao.AppDocumentDAO;
import ru.kotov.dao.AppPhotoDAO;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.AppPhoto;
import ru.kotov.service.FileService;
import ru.kotov.utils.CryptoTool;


@Log4j
@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final CryptoTool cryptoTool;
    @Override
    public AppDocument getDocument(String hash) {
        var id = cryptoTool.idOf(hash);
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        var id = cryptoTool.idOf(hash);
        return appPhotoDAO.findById(id).orElse(null);
    }
}
