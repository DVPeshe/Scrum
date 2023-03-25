package ru.agile.scrum.mst.market.favorite.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.FavoriteDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.favorite.common.SelectorId;
import ru.agile.scrum.mst.market.favorite.mappers.FavoriteMapper;
import ru.agile.scrum.mst.market.favorite.services.FavoriteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final FavoriteMapper favoriteMapper;
    private final SelectorId selectorId;

    @GetMapping("/generate_id")
    public StringResponse generateGuestFavoriteId() {
        return new StringResponse(UUID.randomUUID().toString());
    }

    @GetMapping("/{guestFavoriteId}")
    public FavoriteDto getCurrentFavorite(@RequestHeader(required = false) String username, @PathVariable String guestFavoriteId) {
        Runnable copy = () -> favoriteService.copyFavorite(username, guestFavoriteId);
        String idFavorite = selectorId.selectFavorite(username, guestFavoriteId, copy);
        return favoriteMapper.mapFavoriteToFavoriteDto(favoriteService.getFavorite(idFavorite));
    }

    @GetMapping("/{guestFavoriteId}/add/{productId}")
    public FavoriteDto addProductToFavorite(@RequestHeader(required = false) String username, @PathVariable String guestFavoriteId,
                                    @PathVariable Long productId) {
        String idFavorite = selectorId.selectFavorite(username, guestFavoriteId);
        favoriteService.addToFavorite(idFavorite, productId);
        return favoriteMapper.mapFavoriteToFavoriteDto(favoriteService.getFavorite(idFavorite));
    }

    @GetMapping("/{guestFavoriteId}/clear")
    public void clearFavorite(@RequestHeader(required = false) String username, @PathVariable String guestFavoriteId) {
        String idFavorite = selectorId.selectFavorite(username, guestFavoriteId);
        favoriteService.clearFavorite(idFavorite);
    }

    @GetMapping("/{guestFavoriteId}/delete/{productId}")
    public void deleteProductFromFavorite(@RequestHeader(required = false) String username, @PathVariable String guestFavoriteId,
                                    @PathVariable Long productId) {
        String idFavorite = selectorId.selectFavorite(username, guestFavoriteId);
        favoriteService.removeFromFavorite(idFavorite, productId);
    }

}
