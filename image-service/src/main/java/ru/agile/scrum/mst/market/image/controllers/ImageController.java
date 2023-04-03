package ru.agile.scrum.mst.market.image.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.agile.scrum.mst.market.image.services.ImageService;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public String getImage(@PathVariable String id) {
        return Base64.getEncoder().encodeToString(
                imageService.getImage(id)
                        .orElseThrow(() -> new RuntimeException("Изображение с id: '" + id + "' не найдено!"))
                        .getImage()
                        .getData());
    }

    //еще возможный вариант
//    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] getImageTest1(@PathVariable String id) {
//        return imageService.getImage(id)
//                .orElseThrow(() -> new RuntimeException("Изображение с id: '" + id + "' не найдено!"))
//                .getImage()
//                .getData();
//    }

    //еще возможный вариант
//    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<Resource> getImageTest2(@PathVariable String id){
//        ByteArrayResource bytes = new ByteArrayResource(
//                imageService.getImage(id)
//                .orElseThrow(() -> new RuntimeException("Изображение с id: '" + id + "' не найдено!"))
//                .getImage()
//                .getData());
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentLength(bytes.contentLength())
//                .body(bytes);
//
//    }

    @PostMapping("/add")
    public String addImage(@RequestParam("title") String title, @RequestParam("image") MultipartFile image) throws IOException {
        return imageService.addImage(title, image);
    }
}
