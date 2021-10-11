package com.cnu.spg.board.dto.response;

import com.cnu.spg.board.domain.project.ProjectBoard;
import com.cnu.spg.board.dto.BoardCommentDto;
import com.cnu.spg.board.dto.ProjectUserReferenceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectBoardResponse extends BoardDetailResponse {
    private List<ProjectUserReferenceDto> referenceUsers;

    public ProjectBoardResponse(ProjectBoard board, List<BoardCommentDto> comments, List<ProjectUserReferenceDto> projectRefs) {
        super(board.getId(), board.getTitle(), board.getWriterId(), board.getWriterName(), board.getContent(), comments);
        this.referenceUsers = projectRefs;
    }
}
