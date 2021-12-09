package com.cnu.spg.board.exception;

public class CategoryOmittedException extends RuntimeException {
    private static final String CATEGORY_OMITTED_EXCEPTION_HANDLER = "category에 포함되지 않았습니다.";

    public CategoryOmittedException() {
        super(CATEGORY_OMITTED_EXCEPTION_HANDLER);
    }
}
