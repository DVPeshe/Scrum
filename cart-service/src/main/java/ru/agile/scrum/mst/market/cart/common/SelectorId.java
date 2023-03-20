package ru.agile.scrum.mst.market.cart.common;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SelectorId {
    public String selectCart(String userId, String guestId, Runnable runnableCart) {
        if (userId != null) {
            runnableCart.run();
            return userId;
        }
        return guestId;
    }

    public String selectCart(String userId, String guestId) {
        if (userId != null) {
            return userId;
        }
        return guestId;
    }
}
