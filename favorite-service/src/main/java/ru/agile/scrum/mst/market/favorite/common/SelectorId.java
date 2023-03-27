package ru.agile.scrum.mst.market.favorite.common;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SelectorId {
    public String selectFavorite(String userId, String guestId, Runnable runnable) {
        if (userId != null) {
            runnable.run();
            return userId;
        }
        return guestId;
    }

    public String selectFavorite(String userId, String guestId) {
        if (userId != null) {
            return userId;
        }
        return guestId;
    }
}
