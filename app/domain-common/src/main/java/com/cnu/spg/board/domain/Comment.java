package com.cnu.spg.board.domain;

import com.cnu.spg.comment.exception.CommentAuthException;
import com.cnu.spg.domain.BaseEntity;
import com.cnu.spg.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String writerName;

    private String content;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;


    public void connectBoardWithComment(Board board) {
        board.getComments().add(this);
        this.board = board;
    }

    public void connectUserWithComment(User user) {
        this.user = user;
    }

    private Comment(String writerName, String content) {
        this.writerName = writerName;
        this.content = content;
    }

    public static Comment createComment(User user, Board board, String content) {
        Comment comment = new Comment(user.getName(), content);
        comment.connectBoardWithComment(board);
        comment.connectUserWithComment(user);

        return comment;
    }

    public void editContent(Long userId, String newContent) {
        if (!Objects.equals(user.getId(), userId)) {
            throw new CommentAuthException();
        }
        this.content = newContent;
    }
}
