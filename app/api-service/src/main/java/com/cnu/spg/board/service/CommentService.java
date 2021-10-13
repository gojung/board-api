package com.cnu.spg.board.service;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.Comment;
import com.cnu.spg.board.dto.request.CommentRequest;
import com.cnu.spg.board.exception.BoardNotFoundException;
import com.cnu.spg.board.repository.BoardRepository;
import com.cnu.spg.board.repository.CommentRepository;
import com.cnu.spg.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long saveNewComment(CommentRequest commentRequest, User writerUser) {
        Board board = boardRepository.findById(commentRequest.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        Comment newComment = Comment.createComment(writerUser, board, commentRequest.getContent());

        return commentRepository.save(newComment).getId();
    }
}
