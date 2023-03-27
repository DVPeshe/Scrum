package ru.agile.scrum.mst.market.favorite.utils;

import lombok.Data;
import ru.agile.scrum.mst.market.api.ProductDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Favorite {
    private List<FavoriteItem> items;


    public Favorite() {
        this.items = new ArrayList<>();
    }

    public void add(ProductDto p) {
        for (FavoriteItem item : items) {
            if (Objects.equals(item.getProductId(), p.getId())) {
                return;

            }
        }
        FavoriteItem favoriteItem = new FavoriteItem(p.getId(), p.getTitle(), p.getPrice());
        items.add(favoriteItem);

    }

    public void remove(Long productId) {
        if (items.removeIf(p -> p.getProductId().equals(productId))) {
        }
    }


    public void clear() {
        items.clear();
    }

}
