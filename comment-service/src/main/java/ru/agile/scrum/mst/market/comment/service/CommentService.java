package ru.agile.scrum.mst.market.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.CommentDto;
import ru.agile.scrum.mst.market.comment.entity.Comment;
import ru.agile.scrum.mst.market.comment.exception.FieldsNotNullException;
import ru.agile.scrum.mst.market.comment.repository.CommentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Page<Comment> findAll(int page, int pageSize, Specification<Comment> specification) {
        Sort sort = Sort.by("updatedAt").descending();
        return commentRepository.findAll(specification, PageRequest.of(page, pageSize, sort));
    }

    public Comment createComment(CommentDto commentDto) {
        if (commentDto.getUser() == null) {
            throw new FieldsNotNullException("Авторизуйтесь, чтобы оставить отзыв");
        }
        if (commentDto.getUser() == null || commentDto.getProduct() == null
                || commentDto.getDescription() == null) {
            throw new FieldsNotNullException("Все поля формы должны быть заполнены");
        }
        if (commentDto.getEstimation() == null) {
            throw new FieldsNotNullException("Поставьте оценку");
        }
        Comment comment = new Comment();
        comment.setUsername(commentDto.getUser());
        comment.setProduct(commentDto.getProduct());
        comment.setDescription(commentDto.getDescription());
        comment.setEstimation(commentDto.getEstimation());
        comment.setVisible(true);
        if (commentRepository.existsByUsernameAndProduct(commentDto.getUser(), commentDto.getProduct())) {
            return updateComment(commentDto);
        }
        commentRepository.save(comment);
        return comment;
    }

    public Comment updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.getCommentByUsernameAndProduct(commentDto.getUser(), commentDto.getProduct());
        comment.setUsername(commentDto.getUser());
        comment.setProduct(commentDto.getProduct());
        comment.setDescription(commentDto.getDescription());
        comment.setEstimation(commentDto.getEstimation());
        commentRepository.save(comment);
        return comment;
    }


    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public Double getEstimation(String productTitle) {
        return round(commentRepository.getEstimation(productTitle));
    }

    public static double round(Double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
