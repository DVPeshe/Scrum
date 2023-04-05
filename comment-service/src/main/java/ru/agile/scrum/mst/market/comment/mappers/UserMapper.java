package ru.agile.scrum.mst.market.comment.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.UserCommentDto;
import ru.agile.scrum.mst.market.comment.entity.User;
import ru.agile.scrum.mst.market.comment.repository.UserRepository;

@Component
public class UserMapper {
    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User mapUserCommentDtoToUser(UserCommentDto userCommentDto) {
        User user = User.builder()
                .id(userCommentDto.getId())
                .username(userCommentDto.getUsername())
                .build();
        return user;
    }

    public UserCommentDto mapUserToUserCommentDto(User user) {
        UserCommentDto userCommentDto = UserCommentDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
        return userCommentDto;
    }


}
