package ru.agile.scrum.mst.market.auth.repositories.Specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.agile.scrum.mst.market.auth.entities.User;

public class UsersSpecifications {
    public static Specification<User> titleLike(String titlePart) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), String.format("%%%s%%", titlePart));
    }
}
