package ru.agile.scrum.mst.market.image.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.ImageDto;
import ru.agile.scrum.mst.market.image.models.Image;

@Component
@RequiredArgsConstructor
public class ImageConverter {
    public ImageDto entityToDto (Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .title(image.getTitle())
                .image(image.getImage().getData())
                .build();
    }
}
