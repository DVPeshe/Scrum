package ru.agile.scrum.mst.market.image.tempMigrateMongoDB;

import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.image.models.Image;
import ru.agile.scrum.mst.market.image.repositories.ImageRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class Migration {

    private final ImageRepository imageRepository;

    private static final String DEFAULT_IMAGE_ID = "6426a26deadb6c2a4764b738";

    @PostConstruct
    private void initialize() {

        if (imageRepository.findById(DEFAULT_IMAGE_ID).isPresent()) {
            return;
        }

        byte[] data = null;

        try {
//            data = Files.readAllBytes(Path.of("default/no-photo.jpeg"));                //TODO: для докера
            data = Files.readAllBytes(Path.of("./images/default/no-photo.jpeg"));       //для local
        } catch (IOException e) {
            log.debug("Ошибка чтения файла", e);
        }

        if (data == null) {
            return;
        }

        try {
            Image image = new Image();
            image.setId(DEFAULT_IMAGE_ID);
            image.setTitle("no-photo.jpeg");
            image.setImage(new Binary(BsonBinarySubType.BINARY, data));
            imageRepository.insert(image);
        } catch (MongoException e) {
            log.debug("Ошибка миграции изображений в MongoDB", e);
        }

    }
}
