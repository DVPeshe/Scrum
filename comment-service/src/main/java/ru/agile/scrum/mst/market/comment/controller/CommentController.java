package ru.agile.scrum.mst.market.comment.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.CommentDto;
import ru.agile.scrum.mst.market.comment.entity.Comment;
import ru.agile.scrum.mst.market.comment.exception.ResourceNotFoundException;
import ru.agile.scrum.mst.market.comment.mappers.CommentMapper;
import ru.agile.scrum.mst.market.comment.service.CommentService;
import ru.agile.scrum.mst.market.comment.specifications.CommentSpecifications;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы работы с комментариями")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @GetMapping
    public Page<CommentDto> getAllComments(
            @RequestParam(name = "p", defaultValue = "1") @Parameter(description = "Номер страницы", required = true) Integer page,
            @RequestParam(name = "page_size", defaultValue = "10") @Parameter(description = "Номер страницы", required = false) Integer pageSize,
            @RequestParam(name = "product_id") @Parameter(description = "ID продукта", required = true) Long productId
    ) {
        if (page < 1) {
            page = 1;
        }
        Specification<Comment> spec = Specification.where(null);
        if (productId != null) {
            spec = spec.and(CommentSpecifications.productIdEquals(productId))
                    .and(CommentSpecifications.visibleLike());
        } else {
            spec = spec.and(CommentSpecifications.visibleLike());
        }


        return commentService.findAll(page - 1, pageSize, spec).map(commentMapper::mapCommentToCommentDto);
    }

    @GetMapping("/{id}")
    public CommentDto getProductById(@PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long id) {
        return commentMapper.mapCommentToCommentDto(commentService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт с id: " + id + " не найден")));
    }

}
