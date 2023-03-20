package ru.agile.scrum.mst.market.auth.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.JwtRequest;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.api.UserDto;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.repositories.UserRepository;

@Component
public class UserMapper {
    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto mapUserToUserDto(User user) {
        UserDto userdto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRoles().get(0).getName())
                .access(user.getAccess())
                .build();
        return userdto;
    }

    public JwtRequest mapRegistrationUserDtoToJwtRequest(RegistrationUserDto registrationUserDto) {
        JwtRequest jwtRequest = JwtRequest.builder()
                .password(registrationUserDto.getConfirmPassword())
                .username(registrationUserDto.getUsername())
                .build();
        return jwtRequest;
    }
}
