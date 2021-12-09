package com.cnu.spg.board.domain.project;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.BoardType;
import com.cnu.spg.board.exception.CategoryOmittedException;
import com.cnu.spg.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue(value = BoardType.Values.PROJECT)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectBoard extends Board {
    @ManyToOne
    @JoinColumn(name = "project_category_id")
    private ProjectCategory projectCategory;

    @OneToMany(mappedBy = "projectBoard")
    private List<ProjectReference> projectReference = new ArrayList<>();

    @Builder
    public ProjectBoard(User user, String title, String content, ProjectCategory projectCategory) {
        super(user, title, content);
        if (projectCategory == null) {
            throw new CategoryOmittedException();
        }

        this.projectCategory = projectCategory;
    }
}