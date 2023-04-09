package ru.agile.scrum.mst.market.image.services;

import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.agile.scrum.mst.market.image.models.Image;
import ru.agile.scrum.mst.market.image.repositories.ImageRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Optional<Image> getImage(String id) {
            return imageRepository.findById(id);
    }

    public String addImage(String title, MultipartFile file) throws IOException {
        Image image = new Image();
        image.setTitle(title);
        image.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        image = imageRepository.insert(image);
        return image.getId();
    }
}
