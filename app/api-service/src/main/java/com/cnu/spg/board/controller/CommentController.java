package com.cnu.spg.board.controller;

import com.cnu.spg.board.dto.CommentEditDto;
import com.cnu.spg.board.dto.request.CommentEditRequest;
import com.cnu.spg.board.dto.response.CommentEditResponse;
import com.cnu.spg.board.service.CommentService;
import com.cnu.spg.config.resolver.UserId;
import com.cnu.spg.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/api/v1/boards/{boardId}/comment/{commentId}")
    public ResponseEntity<CommentEditResponse> editComment(@UserId User user,
                                                           @PathVariable("boardId") Long boardId,
                                                           @PathVariable("commentId") Long commentId,
                                                           @Valid @RequestBody CommentEditRequest commentEditRequest) {

        return ResponseEntity.ok(commentService.editComment(CommentEditDto.of(user.getId(), boardId, commentId, commentEditRequest.getContent())));
    }
}
