package ru.agile.scrum.mst.market.comment.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.agile.scrum.mst.market.comment.entity.Comment;



public class CommentSpecifications {

    public static Specification<Comment> visibleLike() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("visible"));

    }

    public static Specification<Comment> productTitleEquals(String product) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("product"), product);
    }
}
