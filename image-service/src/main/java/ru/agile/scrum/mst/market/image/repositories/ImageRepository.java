package ru.agile.scrum.mst.market.image.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.agile.scrum.mst.market.image.models.Image;

public interface ImageRepository extends MongoRepository<Image, String> {
}
