package ru.agile.scrum.mst.market.image.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.ImageDto;
import ru.agile.scrum.mst.market.image.converters.ImageConverter;
import ru.agile.scrum.mst.market.image.services.ImageService;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private  final ImageConverter imageConverter;

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImage(@PathVariable String id){
        return ResponseEntity.ok(imageConverter.entityToDto(imageService.getImage(id)
                        .orElseThrow(() -> new RuntimeException("Изображение с id: '" + id + "' не найдено!"))));

    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestBody ImageDto image) {
        imageService.uploadImage(image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteImage(@PathVariable String id, @RequestBody ImageDto imageDto) {
        imageService.deleteImage(id, imageDto.getProductId());
    }
}
