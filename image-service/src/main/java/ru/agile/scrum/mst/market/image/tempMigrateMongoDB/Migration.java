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
import java.util.Base64;

//Пока такой костыль :)
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

        try {
            Image image = new Image();
            image.setId(DEFAULT_IMAGE_ID);
            image.setTitle("no-photo.jpeg");
            byte[] data = Base64.getDecoder().decode("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");
            image.setImage(new Binary(BsonBinarySubType.BINARY, data));
            imageRepository.insert(image);
        } catch (MongoException e) {
            log.debug("Ошибка миграции изображений в MongoDB", e);
        }

    }
}
