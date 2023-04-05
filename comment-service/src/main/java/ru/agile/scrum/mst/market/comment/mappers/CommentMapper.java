package ru.agile.scrum.mst.market.comment.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.CommentDto;
import ru.agile.scrum.mst.market.comment.entity.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public CommentDto mapCommentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .user(userMapper.mapUserToUserCommentDto(comment.getUser()))
                .product(productMapper.mapProductToProductCommentDto(comment.getProduct()))
                .description(comment.getDescription())
                .build();
    }
}
