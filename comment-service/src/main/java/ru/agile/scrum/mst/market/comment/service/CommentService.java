package ru.agile.scrum.mst.market.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.agile.scrum.mst.market.api.CommentDto;
import ru.agile.scrum.mst.market.comment.entity.Comment;
import ru.agile.scrum.mst.market.comment.exception.FieldsNotNullException;
import ru.agile.scrum.mst.market.comment.mappers.ProductMapper;
import ru.agile.scrum.mst.market.comment.mappers.UserMapper;
import ru.agile.scrum.mst.market.comment.repository.CommentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public Page<Comment> findAll(int page, int pageSize, Specification<Comment> specification) {
        Sort sort = Sort.by("id");
        return commentRepository.findAll(specification, PageRequest.of(page, pageSize, sort));
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.getById(commentDto.getId());
        if (commentDto.getUser() != null) {
            comment.setUser(userMapper.mapUserCommentDtoToUser(commentDto.getUser()));
        }
        if (commentDto.getProduct() != null) {
            comment.setProduct(productMapper.mapProductCommentDtoToProduct(commentDto.getProduct()));
        }
        if (commentDto.getDescription() != null) {
            comment.setDescription(commentDto.getDescription());
        }

        commentRepository.save(comment);
    }

    public Comment createNewComment(CommentDto commentDto) {
        if (commentDto.getUser() == null || commentDto.getProduct() == null
                || commentDto.getDescription() == null) {
            throw new FieldsNotNullException("Все поля формы должны быть заполнены");
        }
        Comment comment = new Comment();
        comment.setUser(userMapper.mapUserCommentDtoToUser(commentDto.getUser()));
        comment.setProduct(productMapper.mapProductCommentDtoToProduct(commentDto.getProduct()));
        comment.setDescription(commentDto.getDescription());
        comment.setVisible(true);
        commentRepository.save(comment);
        return comment;
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void updateVisible(Long id, Boolean visible) {
        Comment comment = commentRepository.getById(id);
        comment.setVisible(visible);
        commentRepository.save(comment);
    }
}
