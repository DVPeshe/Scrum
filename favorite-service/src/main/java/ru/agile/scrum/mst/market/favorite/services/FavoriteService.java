package ru.agile.scrum.mst.market.favorite.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.favorite.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.favorite.utils.Favorite;
import ru.agile.scrum.mst.market.favorite.utils.FavoriteItem;

import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final ProductServiceIntegration productServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplateF;
    private final String prefix = "F";

    public Favorite getFavorite(String idFavorite) {
        String id = prefix + idFavorite;
        if (!redisTemplateF.hasKey(id)) {
            Favorite favorite = new Favorite();
            redisTemplateF.opsForValue().set(id, favorite);
        }
        return (Favorite) redisTemplateF.opsForValue().get(id);
    }

    public void addToFavorite(String idFavorite, Long productId) {
        execute(idFavorite, favorite -> {
            ProductDto p = productServiceIntegration.findById(productId);
            favorite.add(p);
        });
    }


    public void removeFromFavorite(String idFavorite, Long productId) {
        execute(idFavorite, favorite -> favorite.remove(productId));
    }

    public void clearFavorite(String idFavorite) {
        execute(idFavorite, Favorite::clear);
    }

    public void copyFavorite(String userId, String guestId) {
        Favorite guestFavorite = getFavorite(guestId);
        if (!guestFavorite.getItems().isEmpty()) {
            Consumer<Favorite> favoriteConsumer = favorite -> {
                for (FavoriteItem favoriteItem : guestFavorite.getItems()) {
                    favorite.getItems().add(favoriteItem);
                }
            };
            execute(userId, favoriteConsumer);
            clearFavorite(guestId);
        }
    }

    private void execute(String idFavorite, Consumer<Favorite> action) {
        Favorite favorite = getFavorite(idFavorite);
        action.accept(favorite);
        redisTemplateF.opsForValue().set(prefix+idFavorite, favorite);
    }
}
