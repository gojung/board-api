package com.cnu.spg.board.repository;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.dto.condition.BoardSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cnu.spg.board.domain.QBoard.board;
import static com.cnu.spg.utils.QuerydslOrderByUtils.getOrderSpecifier;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression likeContent(String partOfContent) {
        return partOfContent == null ? null : board.content.contains(partOfContent);
    }

    private BooleanExpression likeTitle(String partOfTitle) {
        return partOfTitle == null ? null : board.title.contains(partOfTitle);
    }

    private BooleanExpression eqWriterName(String writerName) {
        return writerName == null ? null : board.writerName.eq(writerName);
    }

    @Override
    public List<Long> findIdsFromPaginationWithKeyword(BoardSearchCondition boardSearchCondition, Pageable pageable) {
        return queryFactory
                .select(board.id)
                .from(board)
                .where(
                        eqWriterName(boardSearchCondition.getWriterName()),
                        likeContent(boardSearchCondition.getContentPart()),
                        likeTitle(boardSearchCondition.getTitlePart())
                )
                .orderBy(getOrderSpecifier(pageable.getSort(), Board.class, "board")
                        .toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public Page<Board> findPageDataFromBoardByIds(List<Long> ids, BoardSearchCondition boardSearchCondition, Pageable pageable) {
        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(new ArrayList<>());
        }

        List<Board> content = queryFactory
                .selectFrom(board)
                .where(board.id.in(ids))
                .fetch()
                .stream()
                .sorted((first, second) -> (int) (-first.getId() + second.getId()))
                .collect(Collectors.toList());

        JPAQuery<Board> countQuery = queryFactory
                .selectFrom(board)
                .where(
                        eqWriterName(boardSearchCondition.getWriterName()),
                        likeContent(boardSearchCondition.getContentPart()),
                        likeTitle(boardSearchCondition.getTitlePart())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }
}
