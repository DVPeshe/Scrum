package ru.agile.scrum.mst.market.image.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.agile.scrum.mst.market.api.ImageDto;
import ru.agile.scrum.mst.market.image.converters.ImageConverter;
import ru.agile.scrum.mst.market.image.models.Image;
import ru.agile.scrum.mst.market.image.services.ImageService;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private  final ImageConverter imageConverter;

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImage(@PathVariable String id){
        return ResponseEntity.ok(imageConverter.entityToDto(imageService.getImage(id)
                        .orElseThrow(() -> new RuntimeException("Изображение с id: '" + id + "' не найдено!"))));

    }

    @PostMapping("/add")
    public String addImage(@RequestParam("title") String title, @RequestParam("image") MultipartFile image) throws IOException {
        return imageService.addImage(title, image);
    }
}
