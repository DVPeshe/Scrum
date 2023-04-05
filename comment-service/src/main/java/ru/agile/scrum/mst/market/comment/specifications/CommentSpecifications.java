package ru.agile.scrum.mst.market.comment.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.agile.scrum.mst.market.comment.entity.Comment;

import java.math.BigDecimal;


public class CommentSpecifications {

    public static Specification<Comment> visibleLike() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("visible"));

    }

    public static Specification<Comment> productIdEquals(Long productId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("product"), productId);
    }
}
