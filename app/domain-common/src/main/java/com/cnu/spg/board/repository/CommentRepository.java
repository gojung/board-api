package com.cnu.spg.board.repository;

import com.cnu.spg.board.domain.Comment;
import com.cnu.spg.board.repository.query.CommentDtoQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentDtoQueryRepository {
}
