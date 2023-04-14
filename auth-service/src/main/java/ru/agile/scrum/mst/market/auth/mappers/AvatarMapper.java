package ru.agile.scrum.mst.market.auth.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.AvatarPersonalAccount;
import ru.agile.scrum.mst.market.auth.entities.Avatar;
import ru.agile.scrum.mst.market.auth.entities.User;

@Component
@RequiredArgsConstructor
public class AvatarMapper {

    public AvatarPersonalAccount map(Avatar avatar) {
        return AvatarPersonalAccount.builder()
                .avatar(avatar.getAvatar())
                .build();
    }

    public Avatar map(AvatarPersonalAccount avatar, User user) {
        return Avatar.builder()
                .id(user.getId())
                .avatar(avatar.getAvatar())
                .user(user)
                .build();
    }
}
