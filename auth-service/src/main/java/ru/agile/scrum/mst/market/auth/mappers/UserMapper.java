package ru.agile.scrum.mst.market.auth.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.UserDto;
import ru.agile.scrum.mst.market.auth.entities.Role;
import ru.agile.scrum.mst.market.auth.entities.User;

@Component
public class UserMapper {

    public UserDto mapUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .rolesTitle(user.getRoles().stream().map(Role::getTitle).toList())
                .access(user.getAccess())
                .build();
    }
}
