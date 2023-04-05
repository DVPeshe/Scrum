package ru.agile.scrum.mst.market.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentDto {
    private Long id;
    private UserCommentDto user;
    private ProductCommentDto product;
    private String description;
}
