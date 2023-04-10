package ru.agile.scrum.mst.market.comment.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.CommentDto;
import ru.agile.scrum.mst.market.comment.entity.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public CommentDto mapCommentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .user(comment.getUsername())
                .product(comment.getProduct())
                .description(comment.getDescription())
                .build();
    }
}
