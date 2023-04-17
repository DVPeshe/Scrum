package ru.agile.scrum.mst.market.image.services;

import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ImageDto;
import ru.agile.scrum.mst.market.image.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.image.models.Image;
import ru.agile.scrum.mst.market.image.repositories.ImageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ProductServiceIntegration productServiceIntegration;

    private static final String DEFAULT_IMAGE_ID = "6426a26deadb6c2a4764b738";

    public Optional<Image> getImage(String id) {
            return imageRepository.findById(id);
    }

    public String uploadImage(ImageDto imageDto){

        Image image = new Image();
        image.setTitle(imageDto.getTitle());
        image.setImage(new Binary(BsonBinarySubType.BINARY, imageDto.getImage()));
        image = imageRepository.insert(image);

        productServiceIntegration.updateImage(imageDto.getProductId(), image.getId());

        return image.getId();
    }

    public void deleteImage(String id, Long productId) {
        if (DEFAULT_IMAGE_ID.equals(id)) {
            return;
        }

        imageRepository.deleteById(id);

        productServiceIntegration.updateImage(productId, DEFAULT_IMAGE_ID);

    }
}
